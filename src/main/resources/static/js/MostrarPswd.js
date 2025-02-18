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