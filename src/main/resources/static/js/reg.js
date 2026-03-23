let currentStep = 0;
const steps = document.querySelectorAll(".form-step");
const indicators = document.querySelectorAll(".step");

function showStep(index) {
	steps.forEach(step => step.classList.remove("active"));
	indicators.forEach(step => step.classList.remove("active"));
	steps[index].classList.add("active");
	indicators[index].classList.add("active");
}

function nextStep() {
	if (currentStep < steps.length - 1) {
		currentStep++;
		showStep(currentStep);
	}
}

function prevStep() {
	if (currentStep > 0) {
		currentStep--;
		showStep(currentStep);
	}
}

showStep(currentStep);

// Backend integration
document.getElementById("registerBtn").addEventListener("click", async function() {
	const formData = {
		firstName: document.getElementById("firstName").value,
		lastName: document.getElementById("lastName").value,
		email: document.getElementById("email").value,
		phone: document.getElementById("phone").value,
		gender: document.getElementById("gender").value,
		maritalStatus: document.getElementById("maritalStatus").value,
		street: document.getElementById("street").value,
		city: document.getElementById("city").value,
		state: document.getElementById("state").value,
		pincode: document.getElementById("pincode").value,
		institutionName: document.getElementById("institutionName").value,
		degree: document.getElementById("degree").value,
		passingYear: document.getElementById("passingYear").value,
		grade: document.getElementById("grade").value,
		percentage: document.getElementById("percentage").value,
		startDate: document.getElementById("startDate").value,
		endDate: document.getElementById("endDate").value,
		companyName: document.getElementById("companyName").value,
		designation: document.getElementById("designation").value,
		ctc: document.getElementById("ctc").value,
		reasonForLeaving: document.getElementById("reasonForLeaving").value,
		currentlyWorking: document.getElementById("currentlyWorking").checked
	};

	const res = await fetch("/api/register", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify(formData)
	});

	alert(await res.text());
});