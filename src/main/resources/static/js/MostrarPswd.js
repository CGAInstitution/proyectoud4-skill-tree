document.addEventListener('DOMContentLoaded', function () {
    const passwordInput = document.getElementById('password');
    const togglePasswordButton = document.getElementById('togglePassword');

    togglePasswordButton.addEventListener('click', function () {
        const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
        passwordInput.setAttribute('type', type);


        togglePasswordButton.textContent = type === 'password' ? 'Mostrar' : 'Ocultar';
    });
});
document.addEventListener('DOMContentLoaded', function () {
    function setupTogglePassword(inputId, buttonId) {
        const passwordInput = document.getElementById(inputId);
        const toggleButton = document.getElementById(buttonId);

        toggleButton.addEventListener('click', function () {
            const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordInput.setAttribute('type', type);
            toggleButton.textContent = type === 'password' ? 'Mostrar' : 'Ocultar';
        });
    }

    setupTogglePassword('contraseña', 'togglePassword');
    setupTogglePassword('confirmar_contraseña', 'toggleConfirmPassword');
    setupTogglePassword('passwordActual', 'toggleCurrentPassword');
});
function showToast() {
    let toast = document.getElementById("toast");
    toast.style.display = "block";

    setTimeout(() => {
        toast.style.display = "none";
    }, 3000);
}

document.addEventListener("DOMContentLoaded", function () {

    let show = document.getElementById("toast").getAttribute("data-show");
    if (show) {
        showToast("Los cambios se han guardado exitosamente.");
    }
});

document.querySelector("form").addEventListener("submit", function(event) {
    let password = document.getElementById("contraseña").value;

    let confirmPassword = document.getElementById("confirmar_contraseña").value;
    let errorSpan = document.getElementById("passwordError");

    if (password){
        if (password !== confirmPassword) {
            event.preventDefault(); // Prevent form submission
            errorSpan.style.display = "inline"; // Show error message
        } else {
            errorSpan.style.display = "none"; // Hide error message
        }
    }

});