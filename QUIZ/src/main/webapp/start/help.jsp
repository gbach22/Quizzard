<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Help</title>
    <link rel="stylesheet" type="text/css" href="../style/helpPageStyle.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
</head>
<body>
    <div class="header">
        <div class="start-page">
            <a href="../start/startPage.jsp">
                <img src="../start/quizzard.png" alt="Quizzard" height="100" width="200">
            </a>
        </div>
        <div class="help-content">
            <h1>Help</h1>
            <div class="icon">
                <i class="fas fa-info-circle"></i>
            </div>
            <p>Welcome to the Help page.<br>Here you'll find answers to frequently asked questions and guides on how to use our platform.</p>
        </div>
    </div>


    <div class="questions">
        <h2>Frequently Asked Questions</h2>
        <div class="question-cards">
            <div class="question-card">
                <h3>How do I create an account?</h3>
                <div class="steps">
                    <p>1. Click on the "Sign Up" or "Register" button located at the top right corner of the homepage.</p>
                    <p>2. Fill in the required information, such as your name, email address, and a password.</p>
                    <p>3. Once verified, you can log in using your email and password.</p>
                    <p>4. If you encounter any issues during registration, please contact our support team at QuizzardSupport@freeuni.edu.ge</p>
                </div>
            </div>
            <div class="question-card">
                <h3>How do I start or take a quiz?</h3>
                <div class="steps">
                    <p>1. Navigate to the "All Quizzes" section from the Home Page.</p>
                    <p>2. Browse through the available quiz categories or use the search bar to find a specific quiz.</p>
                    <p>3. Click on the quiz you want to take and then click the "Start Quiz" button.</p>
                    <p>4. Follow the on-screen instructions and answer the questions.</p>
                </div>
            </div>
            <div class="question-card">
                <h3>How are my scores calculated?</h3>
                <div class="steps">
                    <p>1. Go to the "Create Quiz" section by clicking on the "Create Quiz" link on the Home Page. </p>
                    <p>2. Fill in the quiz details: Enter the quiz title, description, and any necessary instructions.</p>
                    <p>3. Add questions and answers: Choose question type, Input your questions and specify the correct answers.</p>
                    <p>4. Then click "Submit" to make it available to others.</p>
                </div>
            </div>
            <div class="question-card">
                <h3>How can I edit my profile info?</h3>
                <div class="steps">
                    <p>1. Log in to your account by entering your username and password.</p>
                    <p>2. Go to your profile from Home Page by clicking on 'My Profile'.</p>
                    <p>3. Click the "Edit Profile" button to access the profile editing section.</p>
                    <p>4. Update your information in the fields provided, then click "Save Changes" to apply the updates.</p>
                </div>
            </div>
        </div>
    </div>

    <div class="contact">
        <h1>Contact Us</h1>
        <p>Have a question or need assistance? We're here to help! <br> Feel free to reach out to us via email or through our contact form below.</p>
        <p><i class="fas fa-envelope"></i> Email: QuizzardSupport@freeuni.edu.ge</p>
        <p><i class="fas fa-phone"></i> Phone: 032 2 200 901</p>
        <p><i class="fas fa-map-marker-alt"></i> დავით აღმაშენებლის ხეივანი 240, თბილისი.</p>
    </div>

    <footero class="footer">
        <div class="footer-links">
            <a href="../start/terms.jsp">Terms</a>
            <a href="../start/about.jsp">About</a>
        </div>
    </footero>
</body>
</html>
