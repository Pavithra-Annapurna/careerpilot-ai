package com.careerpilot.service;
import com.careerpilot.entity.*; import com.careerpilot.repository.ResumeRepository; import org.apache.pdfbox.Loader; import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.HttpStatus; import org.springframework.stereotype.Service; import org.springframework.web.multipart.MultipartFile; import org.springframework.web.server.ResponseStatusException; import java.io.IOException; import java.util.*;
@Service public class ResumeService {
 private final ResumeRepository repo; private final CurrentUserService current;
 public ResumeService(ResumeRepository r,CurrentUserService c){repo=r;current=c;}
 public record ResumeDto(Long id,String fileName,String createdAt){}
 public ResumeDto upload(MultipartFile f){
  if(f==null||f.isEmpty())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Choose a PDF");
  if(f.getSize()>5L*1024*1024)throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,"Maximum size is 5 MB");
  String name=f.getOriginalFilename()==null?"resume.pdf":f.getOriginalFilename();
  if(!name.toLowerCase().endsWith(".pdf"))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Only PDF files are supported");
  try(var pdf=Loader.loadPDF(f.getBytes())){String text=new PDFTextStripper().getText(pdf).trim();if(text.length()<30)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Could not extract text; scanned PDFs need OCR");Resume r=repo.save(new Resume(current.require(),name,text.substring(0,Math.min(50000,text.length()))));return dto(r);}
  catch(IOException e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid PDF file");}
 }
 public List<ResumeDto> list(){return repo.findAllByUserIdOrderByCreatedAtDesc(current.require().getId()).stream().map(this::dto).toList();}
 public Resume owned(Long id){return repo.findByIdAndUserId(id,current.require().getId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Resume not found"));}
 private ResumeDto dto(Resume r){return new ResumeDto(r.getId(),r.getFileName(),r.getCreatedAt().toString());}
}
