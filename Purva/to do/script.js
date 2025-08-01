function addTask() {
  let input = document.getElementById("taskInput");
  let taskText = input.value.trim();

  if (taskText === "") 
    return;       

  let li = document.createElement("li");
  li.innerHTML = `

    ${taskText}     
    <button class="delete-btn" onclick="deleteTask(this)">Delete</button>
  `;   

  document.getElementById("taskList").appendChild(li);
  input.value = "";
}

function deleteTask(button) {
  let li = button.parentElement;    // li -> task & button
  li.remove();
}
