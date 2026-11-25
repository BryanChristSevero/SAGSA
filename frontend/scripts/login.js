const API_URL = "http://localhost:8080";

document.getElementById("loginForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const email = document.getElementById("username").value;
    const senha = document.getElementById("password").value;
    const alertDiv = document.getElementById("alert");
    alertDiv.style.display = "none";

    try {
        const response = await fetch(`${API_URL}/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, senha })
        });

        if (response.ok) {
            const body = await response.json();
            const token = body.token;
            
            localStorage.setItem("Authorization", token);
            localStorage.setItem("user_email", email);
            window.location.href = "index.html";
        } else {
            alertDiv.innerText = "E-mail ou senha inválidos.";
            alertDiv.style.display = "block";
        }
    } catch (err) {
        alertDiv.innerText = "Erro ao conectar com o servidor da API.";
        alertDiv.style.display = "block";
    }
});