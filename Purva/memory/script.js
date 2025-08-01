const imagePaths = [];
for (let i = 1; i <= 15; i++) {
  imagePaths.push(`https://picsum.photos/seed/${i}/100`);
}

const images = [...imagePaths, ...imagePaths]; // 30 images
shuffle(images);                               //niche

const gameBoard = document.getElementById("gameBoard");
let flippedCards = [];
let moves = 0;
let matchedPairs = 0;

const moveCounter = document.getElementById("moveCounter");

images.forEach((src) => {
  const card = document.createElement("div");
  card.className = "card";

  const img = document.createElement("img");
  img.src = src;


  
  card.appendChild(img);       // image inside the card div
  card.addEventListener("click", () => flipCard(card));

  gameBoard.appendChild(card);
});

function flipCard(card) {
  if (
    card.classList.contains("flipped") ||    // face-up.
    card.classList.contains("locked") ||     // pair
    flippedCards.length === 2
  )
    return;

  card.classList.add("flipped");
  flippedCards.push(card);

  if (flippedCards.length === 2) {
    moves++;
    moveCounter.textContent = `Moves: ${moves}`;

    const [first, second] = flippedCards;
    const img1 = first.querySelector("img").src;
    const img2 = second.querySelector("img").src;

    if (img1 === img2) {
      first.classList.add("locked");
      second.classList.add("locked");
      flippedCards = [];
      matchedPairs++;
      if (matchedPairs === 15) {
        setTimeout(() => alert(`You won in ${moves} moves!`), 500);
      }
    } else setTimeout(() => {
        first.classList.remove("flipped");
        second.classList.remove("flipped");
        flippedCards = [];
      }, 1000);
    }
  }


function shuffle(arr) {
  for (let i = 0; i < arr.length; i++) {
    //  random index 
    const randomIndex = Math.floor(Math.random() * arr.length);

    // Swap with randomIndex
    let temp = arr[i];
    arr[i] = arr[randomIndex];
    arr[randomIndex] = temp;
  }
}

