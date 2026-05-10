# 💰 Personal Finance Tracker Smart System

A desktop-based Personal Finance Management System built with **Java (Swing)** and **MySQL**, developed as a semester project to demonstrate core Object-Oriented Programming principles in a real-world application.

---

## 📸 Screenshots

> Add your project screenshots here after uploading

---

## 🎯 Purpose

Managing personal finances is a challenge for many people. This system provides a simple, secure, and complete solution to track income, control expenses, plan budgets, and view financial reports — all in one desktop application.

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java (JDK 17+) | Core Programming Language |
| Java Swing | Graphical User Interface |
| MySQL | Database |
| JDBC | Java-Database Connectivity |
| VS Code | IDE |

---

## ✨ Features

- 🔐 **User Registration & Login** — Secure authentication with session management
- 💵 **Income Tracking** — Add and view all income records with source and payment method
- 💸 **Expense Tracking** — Category-wise expense management with descriptions
- 📊 **Budget Planner** — Set category limits and get real-time over-budget alerts
- 📈 **Financial Reports** — Complete income vs expense summary
- 🖥️ **Clean Dashboard** — Easy navigation across all modules

---

## 🧠 OOP Concepts Implemented

| Concept | Where Used |
|---|---|
| Inheritance | Income, Expense extend FinancialEntity |
| Abstraction | BaseQuery abstract class |
| Interfaces | Insertable, Retrievable, Deletable |
| Encapsulation | Private fields with getters/setters |
| Polymorphism | getDescription() overridden in each entity |

---

## 📁 Project Structure

Finance/
├── Abstract/           → Base classes (FinancialEntity, BaseQuery)
├── EntityFiles/        → Models (Income, Expense, Budget, User)
├── Query/              → Database query classes
├── gui/                → All UI screens (Login, Dashboard, Income, Expense, Budget, Reports)
├── session/            → User session management
├── db/                 → Database connection (DBConnection)
├── interfaces/         → Insertable, Retrievable, Deletable
├── report/             → Report generation
├── lib/                → MySQL connector JAR
└── MainMethod/         → Main.java entry point
