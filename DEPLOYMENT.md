# 🌐 Cloud Deployment Guide - College ERP System

This guide provides step-by-step instructions for deploying the **College ERP Management System** (Spring Boot + MySQL + Frontend UI) to **Render.com**, **Railway.app**, and **Vercel**.

---

## 📌 Step 1: Push Code to GitHub Repository

1. Open terminal in `d:\Projects\student erp`:
   ```bash
   git init
   git add .
   git commit -m "Initial commit of College ERP System"
   ```
2. Create a new repository on [GitHub](https://github.com/new) named `student-erp`.
3. Link and push your repository:
   ```bash
   git remote add origin https://github.com/YOUR_USERNAME/student-erp.git
   git branch -M main
   git push -u origin main
   ```

---

## 🚀 Deployment Option A: Render.com (Recommended - 1-Click Free Hosting)

[Render](https://render.com) provides free Docker Web Service hosting and MySQL database instances.

### Step-by-Step Instructions:

1. Sign up / Log in to [Render.com](https://dashboard.render.com/).
2. Click **New +** ➔ Select **Blueprint**.
3. Connect your GitHub repository `student-erp`.
4. Render will automatically read `render.yaml` and provision:
   - **`college-erp-db`**: Managed MySQL Database.
   - **`college-erp-system`**: Full-stack Docker Web Service.
5. Click **Apply**. Render will automatically build the container and deploy the app!
6. Once deployed, open your live URL (e.g. `https://college-erp-system.onrender.com`).

---

## 🚀 Deployment Option B: Railway.app (Fastest Container Deployment)

[Railway](https://railway.app) allows instant deployment with a built-in MySQL database plugin.

### Step-by-Step Instructions:

1. Sign up / Log in to [Railway.app](https://railway.app/).
2. Click **New Project** ➔ **Deploy from GitHub repo**.
3. Select your `student-erp` repository.
4. Click **+ Add Service** ➔ Select **Database** ➔ Choose **MySQL**.
5. Railway will automatically link the MySQL environment variables (`MYSQLHOST`, `MYSQLUSER`, `MYSQLPASSWORD`, `MYSQLDATABASE`) to your Spring Boot service.
6. Click on your Web Service ➔ Settings ➔ **Generate Domain**.
7. Open your live Railway URL (e.g. `https://student-erp.up.railway.app`).

---

## ⚡ Deployment Option C: Vercel Frontend + Render/Railway Backend

If you want your frontend hosted on **Vercel** with a `.vercel.app` domain:

1. Deploy the Backend REST API to Render or Railway using Option A or B above.
2. In `src/main/resources/static/js/app.js`, change `const API_BASE = '/api';` to your live backend domain:
   ```js
   const API_BASE = 'https://college-erp-system.onrender.com/api';
   ```
3. Install Vercel CLI or connect repo on [Vercel.com](https://vercel.com/):
   ```bash
   npx vercel
   ```
4. Set Root Directory to `src/main/resources/static` on Vercel.
