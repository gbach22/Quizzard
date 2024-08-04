#1 : ფაილები რომლებზეც ვიმუშავე(createAccount.jsp invalidCreation.jsp, CreateAccountServlet.java User.java UserTests.java)

createAccount.jsp ქმნის create Account page ს და fields შევსების შემთხვევაში button ზე დაჭერისას გადადის Servlet ში

CreateAccountServlet.java გამოიძახება createAccount.jsp დან და invalidCreation.jsp დან, ორივე შემთხვევაში create account
button ზე დაჭერისას. მოცემული Sevrlet ამუშავებს request ის გამოგზავნილ ინფორმაციას, ამოწმებს პირველ რიგში input ის 
ვალიდურობას (თუ input არის არავალიდური ფორმის, ამისამართებს invalidCreation.jsp ში), შემდეგ ამოწმებს (ეს შემოწმებაც არ 
დამიწერია) უკვე არსებობს თუ არა user შემოყვანილი username ით (ამ შემთხვევაშიც ამისამართებს invalidCreation.jsp ში) და 
ბოლოს (რაც ჯერ არ დამიწერია) თუ თუ ყველაფერი სწორადაა და კოდის ბოლო ნაწილში მოვედით ქმნის Session ს ახალ user ს და ამატებს 
მათ შესაბამის ბაზაში (წესით)

invalidCreation.jsp ეკრანზე გამოაჩენს error მესიჯს, რომელსაც servlet request ში უსეტავს, ქმნის create Account page ს და 
fields შევსების შემთხვევაში button ზე დაჭერისას გადადის Servlet ში.

User.java ინახავს user ის ყველა ინფოს (firstName, lastName, userName, password, bio) (არ გამოვრიცხავ რომ დასჭირდეს 
ცვლილებები), getter მეთოდები პირდაპირ აბრუნებს შესაბამის შენახულ ინფოს, მხოლოდ password არის განსხვავებული, ის ჯერ ჰეშავს
შენახულ password ს და ისე აბრუნებს

UserTests.java ტესტავს User ის გეთერებს და ჰეშირების ფუნქციას(აბრუნებს თუ არა ის ყოველთვის უნიკალურ ჰეშს იგივე პაროლისთვის)

#2 : ფაილები რომლებზეც ვიმუშავე (fillInTheBlank.jsp, pictureResponse.jsp, startQuizServlet)

fillInTheBlank.jps და pictureResponse.jsp უზრუნველყოფს შესაბამისი ტიპის კითხვის გამოჩენას ცალკე page ზე. ორივე მათგანს 
ბოლოში მოყვება button next რომელზე დაჭერისასაც გადადის nextQuestionServlet ში (ახალი კითხვის page ს გასახსნელად).

startQuizServlet სტარტტავს არჩეულ ქვიზს, თუ ის არის singlePageMOde მაშინ გადაამისამართებს singlePageJsp ზე (დასაწერია) 
თუ ის არის multiplePageMOde მაშინ განსაზღვრავს პირველი კითხვის ტიპს და შესაბამის jsp ზე გადაამისამართებს.
ასევე quiz.sql ში დავამატე ამ ორი ტიპის კითხვების მაგალითები.
პ.ს შევქმენი homePage.jsp და quiz.jsp (ორივე დასაწერია), რომლებიც დროებით სამუშაოს ასრულებენ ჯერჯერობით, quiz.jsp სამომა-
ვლოდ გამოიძახება homePage ზე რაიმე ქვიზზე დაკლიკების შემთხვევაში. 

#3 :
შევქმენი model კლასი achievements და announcement, ობიექტები რომლებიც ინახავენ შესაბამის ინფორმაციებს.
user.sql ში ჩავამატე ჩემი ექაუნთი (ადმინი). და ყველა ადმინი რომელიც სხვის profile ზე შედის შეუძლია გახადოს ისიც ადმინი,
ამისთვის დამჭირდა makeAdminServlet რომელსაც profile.jsp იძახებს. ასევე profile.jsp იძახებს deleteUserServlet საც, ეს არის
ღილაკი რომელიც ხელმისაწვდომია მხოლოდ ადმინისთვის (სხვის ექაუნთებზე) და ყველა user თვის საკუთარი ექაუნთების წასაშლელად.
ასევე ბაზაში დავამატე achievements_table და announcement_table, ასევე შესაბამისად მათი achievementsDbManager და announcementDbManager, 
ჩავამატე ფუნქციები quizDbManager ში და userDbManager ში, რომლებიც შლიან შესაბამის user ს quiz ს და quizHistory ს.

ადმინის homePage ზე არის დამატებული addAnnouncement ველი რომელიც დადასტურების შემთხვევაში იძახებს adminAddAnnouncementServlet ს
quiz.jsp გავმართე და შესაბამისი ინფორმაციები გამოვაჩინე window ზე, quiz.jsp ზეც მსგავსად profile.jsp სა არის delete quiz button 
რომელიც მხოლოდ ადმინთან ჩანს და ასევე ქვიზის მფლობელთან რომელზე დაჭერის შემთხვევაშიც გამოიძახება deleteQuizServlet. profile.jsp 
ზეც ჩანს უკვე profile ს შესაბამისი ინფორმაცია (ქვემოთ არის ასევე achievements, created quizzes, announcements (თუ ადმინია))

#4 (ბოლო):
დავამატე rating/review system რომელსაც დასჭირდა ratngReviewDbManager, tables in quiz, და singlePageResult ის ცვლილება 
სადაც დასაბმითებამდე ჯერ შესაძლებლობა აქვს user ს შეაფასოს ქუიზი და დაუწეროს review.
დაემატა ასევე addAchievement (სერვლეტით), რომელიც შესაბამისი new achievement მიღების შემთხვევაში გადადის getAchievement.jsp ზე
(რომლის დიზაინიც სრულად გამართულია). add Achievement ლოგიკისთვის დაგვჭირდა რამდენიმე ადგილას კოდის ჩამატებას, კერძოდ:
singlePageResult ში (თუ user მა ამ ქუიზში ყველაზე მაღალი ქულა აიღონ ან მეთე ქუიზი შეავსო), nextQuestionServlet ში, თუ user მა
practice mode ით შეავსო ქუიზი და ბოლოს finishCreatingQuiz ში - თუ user მა (1 ან 5 ან 10) ქუიზი შექმნა.

ასევე დაემატა შესაბამისი მეთოდები delete quiz/user ის სწორად (მთლიანად) წასაშლელად. სრულიად გაიმართა quiz.jsp და adminStatistic.jsp
ფეიჯები (შესაბამისი ინფორმაციის მოსაძიებლად ჩაემატა მეთოდები შესაბამის dbManager ებში), აქვს რამდენიმე განსხვავებული და 
საინტერესო feature არის დამატებული. (ამ ორ jsp რა sevlet ებიც დასჭირდა ყველა დამატებულია). ვიზუალურად და ფუნქციონალურად 
გაიმართა help/terms/about pages.

ბაზაში ჩავამატე რამდენიმე ახალი quiz და დავწერე ტესტები :aanouncementDbManagerTests, AchievementsDbManagerTests, 
StatisticDbManagerTests და AdminStatisticDbManagerTests















