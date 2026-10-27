package com.careerpilot.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.careerpilot.entity.Analysis;
import com.careerpilot.entity.AppUser;
import com.careerpilot.entity.Job;
import com.careerpilot.entity.Resume;
import com.careerpilot.repository.AnalysisRepository;

@Service
public class AnalysisService {

    private final AnalysisRepository repo;
    private final ResumeService resumes;
    private final JobService jobs;
    private final CurrentUserService current;
    private final AiFeedbackService ai;

    public AnalysisService(
            AnalysisRepository r,
            ResumeService rs,
            JobService j,
            CurrentUserService c,
            AiFeedbackService a) {
        repo = r;
        resumes = rs;
        jobs = j;
        current = c;
        ai = a;
    }

    public record Request(Long resumeId, Long jobId) {}

    public record Dto(
            Long id,
            Long resumeId,
            String fileName,
            Long jobId,
            String jobTitle,
            String company,
            int matchScore,
            String matchedSkills,
            String missingSkills,
            String recommendations,
            String createdAt) {}

    @Transactional
    public Dto create(Request req) {
        if (req.resumeId() == null || req.jobId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "resumeId and jobId are required");
        }

        AppUser user = current.require();
        Resume resume = resumes.owned(req.resumeId());
        Job job = jobs.get(req.jobId());

        List<String> required = Arrays.stream(
                        job.getRequiredSkills().split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .distinct()
                .toList();

        String text = resume.getExtractedText()
                .toLowerCase(Locale.ROOT);

        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        for (String skill : required) {
            String s = skill.toLowerCase(Locale.ROOT);

            boolean found = Pattern.compile(
                    "(?<![a-z0-9+#.])" + Pattern.quote(s)
                            + "(?![a-z0-9+#.])")
                    .matcher(text)
                    .find();

            if (found) {
                matched.add(skill);
            } else {
                missing.add(skill);
            }
        }

        int score = required.isEmpty()
                ? 0
                : (int) Math.round(
                        100.0 * matched.size() / required.size());

        String recommendations = ai.feedback(
                resume.getExtractedText(),
                job.getTitle(),
                job.getDescription(),
                missing);

        Analysis analysis = repo.save(new Analysis(
                user,
                resume,
                job,
                score,
                String.join(", ", matched),
                String.join(", ", missing),
                recommendations));

        // Build the response while the transaction is active.
        return dto(analysis);
    }

    @Transactional(readOnly = true)
    public List<Dto> list() {
        return repo.findAllByUserIdOrderByCreatedAtDesc(
                        current.require().getId())
                .stream()
                .map(this::dto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Dto get(Long id) {
        return repo.findByIdAndUserId(
                        id, current.require().getId())
                .map(this::dto)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Analysis not found"));
    }

    private Dto dto(Analysis a) {
        return new Dto(
                a.getId(),
                a.getResume().getId(),
                a.getResume().getFileName(),
                a.getJob().getId(),
                a.getJob().getTitle(),
                a.getJob().getCompany(),
                a.getMatchScore(),
                a.getMatchedSkills(),
                a.getMissingSkills(),
                a.getRecommendations(),
                a.getCreatedAt().toString());
    }
}