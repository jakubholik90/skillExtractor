// dashboard.js - Dashboard functionality

let currentSkills = [];
let currentQuiz = null;
let currentSkillId = null;

// Initialize dashboard
document.addEventListener('DOMContentLoaded', () => {
    checkAuth();
    displayUsername();
    loadProjects();
    loadSkills();
    setupUploadForm();
});

function displayUsername() {
    const username = localStorage.getItem('username');
    if (username) {
        document.getElementById('usernameDisplay').textContent = `Welcome, ${username}`;
    }
}

// Setup upload form
function setupUploadForm() {
    document.getElementById('uploadForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        await uploadProject();
    });
}

// Upload and analyze project
async function uploadProject() {
    const projectName = document.getElementById('projectName').value;
    const description = document.getElementById('description').value;
    const filesInput = document.getElementById('projectFiles');
    const files = filesInput.files;

    if (files.length === 0) {
        alert('Please select files');
        return;
    }

    if (files.length > 20) {
        alert('Maximum 20 files allowed');
        return;
    }

    // Show progress
    const progressDiv = document.getElementById('uploadProgress');
    progressDiv.classList.remove('d-none');
    console.log('Starting upload...');

    try {
        // Read all files
        const fileDataPromises = Array.from(files).map(async (file) => {
            const content = await readFileAsText(file);
            return {
                filename: file.name,
                content: content,
                extension: file.name.split('.').pop()
            };
        });

        const fileData = await Promise.all(fileDataPromises);
        console.log(`Prepared ${fileData.length} files for upload`);

        // Upload to backend
        const response = await apiCall('/projects/upload', 'POST', {
            projectName,
            description,
            files: fileData
        });

        // Hide progress
        progressDiv.classList.add('d-none');

        if (response) {
            console.log('Upload successful:', response);
            alert('Project uploaded and analyzed successfully!');

            // Reset form
            document.getElementById('uploadForm').reset();

            // Reload both projects AND skills
            console.log('Reloading projects and skills...');
            await Promise.all([
                loadProjects(),
                loadSkills()
            ]);

            console.log('Projects and skills reloaded');
        }
    } catch (error) {
        progressDiv.classList.add('d-none');
        console.error('Upload failed:', error);
        alert('Upload failed: ' + error.message);
    }
}

// Load projects
async function loadProjects() {
    console.log('Loading projects...');
    try {
        const projects = await apiCall('/projects');
        console.log('Received projects:', projects);
        displayProjects(projects);
    } catch (error) {
        console.error('Failed to load projects:', error);
    }
}

function displayProjects(projects) {
    const container = document.getElementById('projectsContainer');

    if (!projects || projects.length === 0) {
        console.log('No projects to display');
        container.innerHTML = '<p class="text-muted">No projects uploaded yet.</p>';
        return;
    }

    console.log(`Displaying ${projects.length} projects`);
    container.innerHTML = projects.map(project => `
        <div class="card project-card mb-3" id="project-${project.id}">
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <h5 class="card-title">${escapeHtml(project.name)}</h5>
                        <p class="card-text">${escapeHtml(project.description || 'No description')}</p>
                        <small class="text-muted">
                            Uploaded: ${formatDate(project.uploadedAt)} |
                            Files: ${project.totalFiles} |
                            Size: ${project.totalSizeKb} KB
                        </small>
                    </div>
                    <button class="btn btn-sm btn-outline-danger" onclick="deleteProject(${project.id})" id="delete-btn-${project.id}">
                        Delete
                    </button>
                </div>
            </div>
        </div>
    `).join('');
}

// Delete project function
async function deleteProject(projectId) {
    console.log('Delete project requested:', projectId);

    if (!confirm('Are you sure you want to delete this project? All associated skills will also be deleted.')) {
        console.log('Delete cancelled by user');
        return;
    }

    // Disable delete button and show loading
    const deleteBtn = document.getElementById(`delete-btn-${projectId}`);
    if (deleteBtn) {
        deleteBtn.disabled = true;
        deleteBtn.textContent = 'Deleting...';
    }

    try {
        console.log(`Calling DELETE /api/projects/${projectId}`);
        const response = await apiCall(`/projects/${projectId}`, 'DELETE');
        console.log('Delete response:', response);

        // Show success message
        alert('Project deleted successfully!');

        // Reload projects and skills
        console.log('Reloading projects and skills after delete...');
        await Promise.all([
            loadProjects(),
            loadSkills()
        ]);

        console.log('Projects and skills reloaded after delete');
    } catch (error) {
        console.error('Failed to delete project:', error);
        alert('Failed to delete project: ' + error.message);

        // Re-enable button on error
        if (deleteBtn) {
            deleteBtn.disabled = false;
            deleteBtn.textContent = 'Delete';
        }
    }
}

// Load skills
async function loadSkills() {
    console.log('Loading skills...');
    try {
        const skills = await apiCall('/skills');
        console.log('Received skills:', skills);
        currentSkills = skills;
        displaySkills(skills);
    } catch (error) {
        console.error('Failed to load skills:', error);
    }
}

function displaySkills(skills) {
    const container = document.getElementById('skillsContainer');

    if (!skills || skills.length === 0) {
        console.log('No skills to display');
        container.innerHTML = '<p class="text-muted">No skills extracted yet. Upload a project to get started!</p>';
        return;
    }

    console.log(`Displaying ${skills.length} skills`);

    // Group by category
    const groupedSkills = skills.reduce((acc, skill) => {
        if (!acc[skill.category]) {
            acc[skill.category] = [];
        }
        acc[skill.category].push(skill);
        return acc;
    }, {});

    container.innerHTML = Object.entries(groupedSkills).map(([category, categorySkills]) => `
        <div class="category-section">
            <div class="category-header">
                <h5 class="mb-0">${escapeHtml(categorySkills[0].categoryDisplayName)}</h5>
            </div>
            <div class="row">
                ${categorySkills.map(skill => `
                    <div class="col-md-6 mb-3">
                        <div class="card skill-card" onclick="openQuiz(${skill.id})">
                            <div class="card-body">
                                <h6 class="card-title">${escapeHtml(skill.name)}</h6>
                                <p class="card-text small">${escapeHtml(skill.description)}</p>
                                <div class="d-flex justify-content-between align-items-center">
                                    <span class="badge bg-primary badge-category">${escapeHtml(skill.categoryDisplayName)}</span>
                                    <span class="skill-level ${getSkillLevelClass(skill.level)}">${skill.levelDisplay}</span>
                                </div>
                                <small class="text-muted d-block mt-2">From: ${escapeHtml(skill.projectName)}</small>
                            </div>
                        </div>
                    </div>
                `).join('')}
            </div>
        </div>
    `).join('');
}

// Open quiz modal
async function openQuiz(skillId) {
    currentSkillId = skillId;
    console.log('Opening quiz for skill ID:', skillId);

    // Pokaż modal z loader'em od razu
    const modal = new bootstrap.Modal(document.getElementById('quizModal'));
    modal.show();

    // Znajdź nazwę skilla
    const skill = currentSkills.find(s => s.id === skillId);
    const skillName = skill ? skill.name : 'Skill';

    document.getElementById('quizModalTitle').textContent = `Quiz: ${skillName}`;

    // Pokaż loading state
    const content = document.getElementById('quizContent');
    content.innerHTML = `
        <div class="quiz-loading">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
            <div class="quiz-loading-text">Generating quiz questions...</div>
            <div class="quiz-loading-subtext">This may take up to 30 seconds</div>
        </div>
    `;

    try {
        // Wywołaj API w tle
        const quiz = await apiCall(`/quiz/generate/${skillId}`, 'POST');
        console.log('Quiz generated:', quiz);
        currentQuiz = quiz;

        // Wyświetl quiz po wygenerowaniu
        displayQuiz(quiz);
    } catch (error) {
        console.error('Quiz generation failed:', error);
        content.innerHTML = `
            <div class="alert alert-danger">
                <h5>Failed to generate quiz</h5>
                <p>${escapeHtml(error.message)}</p>
                <button class="btn btn-primary" data-bs-dismiss="modal">Close</button>
            </div>
        `;
    }
}

function displayQuiz(quiz) {
    document.getElementById('quizModalTitle').textContent = `Quiz: ${quiz.skillName}`;

    const content = document.getElementById('quizContent');
    content.innerHTML = `
        <form id="quizForm">
            ${quiz.questions.map(q => {
                // Przetwórz tekst pytania - zamień markdown code blocks
                const questionText = formatQuestionText(q.text);

                return `
                    <div class="quiz-question">
                        <p><strong>Question ${q.number}:</strong></p>
                        <div class="question-content">${questionText}</div>
                        ${q.options.map((option, idx) => `
                            <div class="quiz-option">
                                <input type="radio" name="q${q.number}" value="${option.charAt(0)}" id="q${q.number}_${idx}" required>
                                <label for="q${q.number}_${idx}">${escapeHtml(option)}</label>
                            </div>
                        `).join('')}
                    </div>
                `;
            }).join('')}
            <button type="submit" class="btn btn-primary">Submit Quiz</button>
        </form>
    `;

    // Zastosuj syntax highlighting do wszystkich code blocks
    document.querySelectorAll('pre code').forEach((block) => {
        hljs.highlightElement(block);
    });

    document.getElementById('quizForm').addEventListener('submit', submitQuiz);
}

// Formatuj tekst pytania - zamień markdown na HTML
function formatQuestionText(text) {
    if (!text) return '';

    // Zamień triple backticks (```java ... ```) na <pre><code>
    text = text.replace(/```(\w+)?\n([\s\S]*?)```/g, function(match, lang, code) {
        lang = lang || 'java';
        const escapedCode = escapeHtml(code.trim());
        return `<pre><code class="language-${lang}">${escapedCode}</code></pre>`;
    });

    // Zamień single backticks (`code`) na inline code
    text = text.replace(/`([^`]+)`/g, '<code>$1</code>');

    // Zamień newlines na <br> (ale nie wewnątrz code blocks)
    text = text.replace(/\n/g, '<br>');

    return text;
}

async function submitQuiz(e) {
    e.preventDefault();
    console.log('Submitting quiz for skill ID:', currentSkillId);

    const answers = currentQuiz.questions.map(q => ({
        questionNumber: q.number,
        selectedAnswer: document.querySelector(`input[name="q${q.number}"]:checked`).value
    }));

    try {
        const result = await apiCall('/quiz/submit', 'POST', {
            skillId: currentSkillId,
            answers: answers
        });

        console.log('Quiz result:', result);
        displayQuizResult(result);
        loadSkills(); // Refresh skills to show updated level
    } catch (error) {
        console.error('Quiz submission failed:', error);
        alert('Failed to submit quiz: ' + error.message);
    }
}

function displayQuizResult(result) {
    const content = document.getElementById('quizContent');
    const levelClass = getSkillLevelClass(result.achievedLevel);

    content.innerHTML = `
        <div class="quiz-result alert alert-info">
            <h4>Quiz Complete!</h4>
            <p class="mb-2"><strong>Score:</strong> ${result.score}%</p>
            <p class="mb-2"><strong>Correct Answers:</strong> ${result.correctAnswers} / ${result.totalQuestions}</p>
            <p class="mb-0"><strong>Skill Level:</strong> <span class="skill-level ${levelClass}">${result.levelDisplay}</span></p>
            <button class="btn btn-primary mt-3" data-bs-dismiss="modal">Close</button>
        </div>
    `;
}

// Utility function to escape HTML and prevent XSS
function escapeHtml(text) {
    if (!text) return '';
    const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };
    return text.replace(/[&<>"']/g, m => map[m]);
}