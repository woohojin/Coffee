document.addEventListener('DOMContentLoaded', () => {
  const urlParams = new URLSearchParams(window.location.search);
  const errorCode = urlParams.get("code");
  const errorMessage = urlParams.get("message");

  const errorCodeElement = document.getElementById("error-code");
  const errorMessageElement = document.getElementById("error-message");

  if (errorCode !== null && errorMessage !== null) {
    if (errorCodeElement) errorCodeElement.textContent = errorCode;
    if (errorMessageElement) errorMessageElement.textContent = errorMessage;
  }
});