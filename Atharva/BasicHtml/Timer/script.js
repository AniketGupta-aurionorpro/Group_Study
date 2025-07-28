let countdown;
let totalTime = 60;
let isRunning = false;

function startTimer() {
  if (isRunning) return;

  isRunning = true;
  document.getElementById('startBtn').disabled = true;
  document.getElementById('pauseBtn').disabled = false;

  countdown = setInterval(() => {
    if (totalTime <= 0) {
      clearInterval(countdown);
      isRunning = false;
      document.getElementById('pauseBtn').disabled = true;
      return;
    }
    totalTime--;
    updateDisplay();
  }, 1000);

  updateDisplay();
}

function pauseTimer() {
  if (!isRunning) return;

  clearInterval(countdown);
  isRunning = false;
  document.getElementById('startBtn').disabled = false;
  document.getElementById('pauseBtn').disabled = true;
}

function resetTimer() {
  clearInterval(countdown);
  totalTime = 60;
  isRunning = false;
  updateDisplay();

  document.getElementById('startBtn').disabled = false;
  document.getElementById('pauseBtn').disabled = true;
}

function updateDisplay() {
  const minutes = Math.floor(totalTime / 60);
  const seconds = totalTime % 60;
  document.getElementById('display').textContent =
    `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
}
