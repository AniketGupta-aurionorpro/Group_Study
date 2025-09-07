<!-- Message display section -->
<% String errorMsg = (String) session.getAttribute("errorMessage");
   String successMsg = (String) session.getAttribute("successMessage");
   if(errorMsg != null) { %>
       <p class="error"><%= errorMsg %></p>
       <% session.removeAttribute("errorMessage"); %>
<% } %>
<% if(successMsg != null) { %>
       <p class="success"><%= successMsg %></p>
       <% session.removeAttribute("successMessage"); %>
<% } %>
