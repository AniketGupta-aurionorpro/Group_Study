let time=25*60;
let timerId=null;

function showTimer(){
    const minutes = Math.floor(time / 60);
  const seconds = time % 60;
  document.getElementById('timer').textContent =
    `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
} 
function startTimer() {
  if (timerId) return;
  timerId = setInterval(() => {
    time--;
    showTimer();
    if (time <= 0) {
      clearInterval(timerId);
      timerId = null;
      time = 25 * 60;
      showTimer();
    }
  }, 1000);
}

function stopTimer() {
  clearInterval(timerId);
  timerId = null;
}

function resetTimer() {
  clearInterval(timerId);
  timerId = null;
  time = 25 * 60;
  showTimer();
}

showTimer();