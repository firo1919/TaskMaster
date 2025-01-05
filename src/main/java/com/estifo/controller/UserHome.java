package com.estifo.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.estifo.beans.Category;
import com.estifo.beans.Task;
import com.estifo.beans.User;
import com.estifo.service.TaskDAO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class UserHome extends HttpServlet {

    public UserHome() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String category = request.getParameter("category");
        User user = (User) session.getAttribute("user");
        try {
            if (user != null) {
                TaskDAO taskDAO = new TaskDAO();
                List<Task> tasks = taskDAO.getByCategory(Category.valueOf(category), user);
                List<String> categories = List.of(Category.values()).stream().map(Category::name)
                        .collect(Collectors.toList());

                request.setAttribute("categories", categories);
                request.setAttribute("tasks", tasks);
                request.setAttribute("currentCategory", category);
                RequestDispatcher dis = request.getRequestDispatcher("WEB-INF/dashboard.jsp");
                dis.forward(request, response);
            } else {
                System.out.println("User is null");
                request.setAttribute("error", "Invalid email or password");
                RequestDispatcher dis = request.getRequestDispatcher("login.jsp");
                dis.forward(request, response);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
