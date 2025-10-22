// app.js - Common utility functions

// API Base URL
const API_BASE = '/api';

// Authentication helpers
function getAuthHeader() {
    const credentials = localStorage.getItem('credentials');
    if (!credentials) {
        window.location.href = '/login.html';
        return null;
    }
    return {
        'Authorization': `Basic ${credentials}`,
        'Content-Type': 'application/json'
    };
}

function saveCredentials(username, password) {
    const credentials = btoa(`${username}:${password}`);
    localStorage.setItem('credentials', credentials);
    localStorage.setItem('username', username);
}

function logout() {
    localStorage.removeItem('credentials');
    localStorage.removeItem('username');
    window.location.href = '/index.html';
}

function checkAuth() {
    const credentials = localStorage.getItem('credentials');
    if (!credentials) {
        window.location.href = '/login.html';
    }
}

// API call wrapper
async function apiCall(endpoint, method = 'GET', body = null) {
    const headers = getAuthHeader();
    if (!headers) return null;

    const options = {
        method,
        headers
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    try {
        const response = await fetch(`${API_BASE}${endpoint}`, options);

        if (response.status === 401) {
            logout();
            return null;
        }

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Request failed');
        }

        return await response.json();
    } catch (error) {
        console.error('API call failed:', error);
        throw error;
    }
}

// File reading helper
function readFileAsText(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = (e) => resolve(e.target.result);
        reader.onerror = (e) => reject(e);
        reader.readAsText(file);
    });
}

// Format date
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    });
}

// Get skill level class
function getSkillLevelClass(level) {
    const levelMap = {
        'UNKNOWN': 'level-unknown',
        'BASIC': 'level-basic',
        'GOOD': 'level-good',
        'EXPERT': 'level-expert'
    };
    return levelMap[level] || 'level-unknown';
}

// Show toast notification (simple alert for MVP)
function showToast(message, type = 'info') {
    alert(message);
}