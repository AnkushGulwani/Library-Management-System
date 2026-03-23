document.getElementById("loginForm").addEventListener("submit", async function(e) {
  e.preventDefault();

  const email = e.target.email.value;
  const password = e.target.password.value;

  const res = await fetch(`/api/login?email=${email}&password=${password}`, {
    method: "POST"
  });

  document.getElementById("loginResponse").innerText = await res.text();
});