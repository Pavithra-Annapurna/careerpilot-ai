
import { useEffect, useState, useRef } from "react";
import {
  Sparkles,
  FileText,
  BriefcaseBusiness,
  Target,
  Activity,
  UploadCloud,
  ArrowRight,
  LogOut,
  CheckCircle2,
  Search,
} from "lucide-react";
import api from "./api";

const skills = (value) =>
  (value || "").split(",").map((s) => s.trim()).filter(Boolean);

function Stat({ icon: Icon, label, value }) {
  return (
    <article className="stat">
      <div className="statlabel">
        <span>{label}</span>
        <Icon size={18} />
      </div>
      <b>{value}</b>
      <small>Updated from your workspace</small>
    </article>
  );
}

export default function App() {
  const [token, setToken] = useState(
    localStorage.getItem("careerpilot_token") || ""
  );
  const [user, setUser] = useState(() =>
    JSON.parse(localStorage.getItem("careerpilot_user") || "null")
  );
  const [mode, setMode] = useState("login");
  const [auth, setAuth] = useState({
    name: "",
    email: "",
    password: "",
  });
  const [jobs, setJobs] = useState([]);
  const [resumes, setResumes] = useState([]);
  const [analyses, setAnalyses] = useState([]);
  const [resumeId, setResumeId] = useState("");
  const [jobId, setJobId] = useState("");
  const [file, setFile] = useState(null);
  const [result, setResult] = useState(null);
  const [msg, setMsg] = useState("");
  const [busy, setBusy] = useState(false);
  const [search, setSearch] = useState("");

  // Reference to the Resume Analyzer section
  const analyzerRef = useRef(null);

  async function load() {
    try {
      const [jobsResponse, resumesResponse, analysesResponse] =
        await Promise.all([
          api.get("/jobs"),
          api.get("/resumes"),
          api.get("/analyses"),
        ]);

      setJobs(jobsResponse.data);
      setResumes(resumesResponse.data);
      setAnalyses(analysesResponse.data);

      if (!resumeId && resumesResponse.data[0]) {
        setResumeId(String(resumesResponse.data[0].id));
      }

      if (!jobId && jobsResponse.data[0]) {
        setJobId(String(jobsResponse.data[0].id));
      }
    } catch (error) {
      setMsg(
        error.response?.data?.detail ||
          error.response?.data?.message ||
          "Could not load data. Is the backend running?"
      );
    }
  }

  useEffect(() => {
    if (token) load();
  }, [token]);

  async function authSubmit(event) {
    event.preventDefault();
    setBusy(true);

    try {
      const { data } = await api.post(`/auth/${mode}`, auth);
      const userData = {
        name: data.name,
        email: data.email,
      };

      localStorage.setItem("careerpilot_token", data.token);
      localStorage.setItem(
        "careerpilot_user",
        JSON.stringify(userData)
      );

      setToken(data.token);
      setUser(userData);
      setMsg("");
    } catch (error) {
      setMsg(
        error.response?.data?.detail ||
          error.response?.data?.message ||
          "Authentication failed"
      );
    } finally {
      setBusy(false);
    }
  }

  function logout() {
  // Clear saved login session
  localStorage.removeItem("careerpilot_token");
  localStorage.removeItem("careerpilot_user");

  // Clear authentication state
  setToken("");
  setUser(null);
  setMode("login");
  setAuth({
    name: "",
    email: "",
    password: "",
  });

  // Clear application data
  setAnalyses([]);
  setResumes([]);
  setJobs([]);
  setResult(null);
  setResumeId("");
  setJobId("");
  setFile(null);
  setSearch("");
  setMsg("");
}

  async function upload(event) {
    event.preventDefault();

    if (!file) {
      setMsg("Choose a PDF first.");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);
    setBusy(true);

    try {
      const { data } = await api.post("/resumes", formData);

      setMsg(`Uploaded ${data.fileName || file.name}`);
      setFile(null);

      event.target.reset();
      await load();
    } catch (error) {
      setMsg(
        error.response?.data?.detail ||
          error.response?.data?.message ||
          "Upload failed"
      );
    } finally {
      setBusy(false);
    }
  }

  async function analyze(event) {
    event.preventDefault();

    if (!resumeId || !jobId) {
      setMsg("Select a resume and target role first.");
      return;
    }

    setBusy(true);

    try {
      const { data } = await api.post("/analyses", {
        resumeId: Number(resumeId),
        jobId: Number(jobId),
      });

      setResult(data);
      setMsg(
        "Analysis complete. The match score is a heuristic, not an ATS score."
      );

      await load();
    } catch (error) {
      setMsg(
        error.response?.data?.detail ||
          error.response?.data?.message ||
          "Analysis failed"
      );
    } finally {
      setBusy(false);
    }
  }

  // Select a role and move the user to the analyzer
  function handleSelectRole(job) {
    setJobId(String(job.id));
    setMsg(
      `Selected ${job.title} at ${job.company}. Choose a resume and click Analyze resume.`
    );

    analyzerRef.current?.scrollIntoView({
      behavior: "smooth",
      block: "start",
    });
  }

  const filteredJobs = jobs.filter((job) =>
    `${job.title || ""} ${job.company || ""} ${
      job.requiredSkills || ""
    }`
      .toLowerCase()
      .includes(search.toLowerCase())
  );

  if (!token) {
    return (
      <main className="auth">
        <div className="brand">
          <span className="mark">
            <Sparkles />
          </span>
          careerpilot<span className="purple">.ai</span>
        </div>

        <section className="authcard">
          <p className="eyebrow">YOUR CAREER, IN FOCUS</p>

          <h1>
            {mode === "login"
              ? "Welcome back"
              : "Create your account"}
          </h1>

          <p className="muted">
            Analyze your resume, discover roles and find your
            next skill to build.
          </p>

          <form onSubmit={authSubmit} className="form">
            {mode === "register" && (
              <label>
                Name
                <input
                  required
                  value={auth.name}
                  onChange={(event) =>
                    setAuth({
                      ...auth,
                      name: event.target.value,
                    })
                  }
                />
              </label>
            )}

            <label>
              Email
              <input
                type="email"
                required
                value={auth.email}
                onChange={(event) =>
                  setAuth({
                    ...auth,
                    email: event.target.value,
                  })
                }
              />
            </label>

            <label>
              Password
              <input
                type="password"
                minLength={8}
                required
                value={auth.password}
                onChange={(event) =>
                  setAuth({
                    ...auth,
                    password: event.target.value,
                  })
                }
              />
            </label>

            <button
              type="submit"
              className="primary"
              disabled={busy}
            >
              {busy
                ? "Please wait..."
                : mode === "login"
                  ? "Sign in"
                  : "Create account"}
              <ArrowRight size={16} />
            </button>
          </form>

          {msg && <p className="notice">{msg}</p>}

          <p className="muted center">
            {mode === "login"
              ? "New here?"
              : "Already registered?"}{" "}
            <button
              type="button"
              className="link"
              onClick={() => {
                setMode(
                  mode === "login" ? "register" : "login"
                );
                setMsg("");
              }}
            >
              {mode === "login"
                ? "Create account"
                : "Sign in"}
            </button>
          </p>
        </section>
      </main>
    );
  }

  return (
    <div className="layout">
      <aside className="sidebar">
        <div className="brand">
          <span className="mark">
            <Sparkles />
          </span>
          careerpilot<span className="purple">.ai</span>
        </div>

        <p className="side-label">WORKSPACE</p>

        <div className="userbox">
          <div className="avatar">
            {user?.name?.[0] || "U"}
          </div>
          <div>
            <b>{user?.name}</b>
            <small>{user?.email}</small>
          </div>
        </div>

        <button
          type="button"
          className="logout"
          onClick={logout}
        >
          <LogOut size={16} /> Sign out
        </button>

        <p className="side-note">
          AI-assisted guidance. Your career decisions stay yours.
        </p>
      </aside>

      <main className="main">
        <header>
          <div>
            <small>WORKSPACE / OVERVIEW</small>
            <h1>
              Make your next move{" "}
              <span className="purple">count.</span>
            </h1>
            <p className="muted">
              Understand your strengths and find the skills
              that move you forward.
            </p>
          </div>

          <span className="pill">● Workspace active</span>
        </header>

        {msg && (
          <div className="notice">
            {msg}{" "}
            <button
              type="button"
              className="link"
              onClick={() => setMsg("")}
            >
              Dismiss
            </button>
          </div>
        )}

        <section className="stats">
          <Stat
            icon={FileText}
            label="Resumes uploaded"
            value={resumes.length}
          />

          <Stat
            icon={Target}
            label="Analyses completed"
            value={analyses.length}
          />

          <Stat
            icon={BriefcaseBusiness}
            label="Available roles"
            value={jobs.length}
          />

          <Stat
            icon={Activity}
            label="Average match"
            value={
              analyses.length
                ? `${Math.round(
                    analyses.reduce(
                      (sum, item) =>
                        sum + (item.matchScore || 0),
                      0
                    ) / analyses.length
                  )}%`
                : "—"
            }
          />
        </section>

        <div className="columns">
          {/* Resume Analyzer */}
          <section
            className="panel"
            ref={analyzerRef}
            id="resume-analyzer"
          >
            <h2>
              <UploadCloud /> Resume analyzer
            </h2>

            <p className="muted">
              Upload a text-based PDF, then compare it with a
              target role.
            </p>

            <form onSubmit={upload} className="form">
              <label className="filebox">
                Choose PDF (max 5 MB)
                <input
                  type="file"
                  accept="application/pdf,.pdf"
                  onChange={(event) =>
                    setFile(
                      event.target.files?.[0] || null
                    )
                  }
                />
                {file?.name || "No file selected"}
              </label>

              <button
                type="submit"
                className="primary"
                disabled={busy || !file}
              >
                {busy ? "Uploading..." : "Upload resume"}
                <ArrowRight size={16} />
              </button>
            </form>

            <h3>Saved resumes</h3>

            {resumes.length ? (
              resumes.map((resume) => (
                <button
                  type="button"
                  className={`selectrow ${
                    resumeId === String(resume.id)
                      ? "selected"
                      : ""
                  }`}
                  key={resume.id}
                  onClick={() =>
                    setResumeId(String(resume.id))
                  }
                >
                  <FileText size={17} />
                  {resume.fileName}
                  {resumeId === String(resume.id) && (
                    <CheckCircle2 size={16} />
                  )}
                </button>
              ))
            ) : (
              <p className="muted">
                Upload your first resume.
              </p>
            )}

            <hr />

            <h2>
              <Sparkles /> Run an analysis
            </h2>

            <form onSubmit={analyze} className="form">
              <label>
                Resume
                <select
                  value={resumeId}
                  onChange={(event) =>
                    setResumeId(event.target.value)
                  }
                  required
                >
                  <option value="">Select resume</option>
                  {resumes.map((resume) => (
                    <option
                      key={resume.id}
                      value={resume.id}
                    >
                      {resume.fileName}
                    </option>
                  ))}
                </select>
              </label>

              <label>
                Target role
                <select
                  value={jobId}
                  onChange={(event) =>
                    setJobId(event.target.value)
                  }
                  required
                >
                  <option value="">Select role</option>
                  {jobs.map((job) => (
                    <option key={job.id} value={job.id}>
                      {job.title} — {job.company}
                    </option>
                  ))}
                </select>
              </label>

              <button
                type="submit"
                className="primary"
                disabled={
                  busy || !resumes.length || !jobs.length
                }
              >
                {busy ? "Analyzing..." : "Analyze resume"}
                <ArrowRight size={16} />
              </button>
            </form>

            {result && (
              <div className="result">
                <div className="score">
                  {result.matchScore}%
                </div>

                <div>
                  <b>{result.jobTitle}</b>
                  <p className="muted">
                    Required-skill coverage · heuristic only
                  </p>
                </div>

                <div className="resultgrid">
                  <div>
                    <h4>Matched skills</h4>
                    <p>
                      {result.matchedSkills || "None found"}
                    </p>
                  </div>

                  <div>
                    <h4>Skills to develop</h4>
                    <p>
                      {result.missingSkills ||
                        "No missing required skills detected"}
                    </p>
                  </div>
                </div>

                <h4>Recommendations</h4>
                <ul>
                  {(() => {
                    try {
                      const recommendations = JSON.parse(
                        result.recommendations
                      );

                      return Array.isArray(recommendations)
                        ? recommendations.map(
                            (item, index) => (
                              <li key={index}>{item}</li>
                            )
                          )
                        : (
                            <li>
                              {String(
                                result.recommendations ||
                                  "No recommendations available."
                              )}
                            </li>
                          );
                    } catch {
                      return (
                        <li>
                          {result.recommendations ||
                            "No recommendations available."}
                        </li>
                      );
                    }
                  })()}
                </ul>
              </div>
            )}
          </section>

          {/* Explore Roles and Analysis History */}
          <section className="panel">
            <h2>
              <BriefcaseBusiness /> Explore roles
            </h2>

            <div className="search">
              <Search size={16} />
              <input
                placeholder="Search jobs or skills"
                value={search}
                onChange={(event) =>
                  setSearch(event.target.value)
                }
              />
            </div>

            <div className="joblist">
              {filteredJobs.map((job) => (
                <article className="job" key={job.id}>
                  <div className="jobtop">
                    <h3>{job.title}</h3>
                    <span className="company">
                      {job.company}
                    </span>
                  </div>

                  <p className="muted">
                    {job.description}
                  </p>

                  <div className="chips">
                    {skills(job.requiredSkills).map(
                      (skill) => (
                        <span key={skill}>{skill}</span>
                      )
                    )}
                  </div>

                  <button
                    type="button"
                    className="secondary"
                    onClick={() => handleSelectRole(job)}
                  >
                    Select this role{" "}
                    <ArrowRight size={14} />
                  </button>
                </article>
              ))}

              {!filteredJobs.length && (
                <p className="muted">
                  No roles match your search.
                </p>
              )}
            </div>

            <hr />

            <h2>
              <Activity /> Analysis history
            </h2>

            {analyses.length ? (
              analyses.map((analysis) => (
                <button
                  type="button"
                  key={analysis.id}
                  className="history"
                  onClick={() => {
                    setResult(analysis);
                    analyzerRef.current?.scrollIntoView({
                      behavior: "smooth",
                      block: "start",
                    });
                  }}
                >
                  <span>
                    <b>{analysis.jobTitle}</b>
                    <small>
                      {analysis.fileName} · {analysis.company}
                    </small>
                  </span>
                  <strong>{analysis.matchScore}%</strong>
                </button>
              ))
            ) : (
              <p className="muted">
                Your saved analyses will appear here.
              </p>
            )}
          </section>
        </div>

        <footer>
          CareerPilot AI · Built with React, Spring Boot,
          MySQL and AI-assisted feedback.
        </footer>
      </main>
    </div>
  );
}
