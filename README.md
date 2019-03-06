# SpringBoot+OAuth2+SSO+ReactJS+Antd
## This repository consists of 4 projects:
1. oauthserver<br/>
    acts as the SSO OAuth Server side
2. oauthclientprofile<br/>
    acts as the SSO OAuth Client side
3. loginUI<br/>
    based on ReactJS+Antd, the generated UI will be used in oauthserver
4. profileUI<br/>
    based on ReactJS+Antd, the generated UI will be used in oauthclientprofile
    
## How to use them:
1. oauthserver<br/>
    in https://github.com/gfso2000/together/blob/master/oauthserver/src/main/resources/application.properties, <br/>
    change "spring.mail.username" and "spring.mail.password" to your gmail username/password<br/>
    
    execute sql in MySQL to create db schema:<br/>
    https://github.com/gfso2000/together/blob/master/oauthserver/src/main/resources/mysql_script/sql.txt<br/>
    
2. oauthclientprofile<br/>
    in https://github.com/gfso2000/together/blob/master/oauthclientprofile/src/main/resources/application.properties,<br/>
    change "spring.mail.username" and "spring.mail.password" to your gmail username/password<br/>
    
3. loginUI<br/>
    run "yarn start" to start in dev mode<br/>
    run "npm run build" to build the final html/js/css<br/>
    copy dist/umi.css to oauthserver\src\main\resources\static\css folder<br/>
    copy dist/umi.js to oauthserver\src\main\resources\static\js folder<br/>
    copy dist/index.html to oauthserver\src\main\resources\templates\login.html<br/>
    modify login.html, change umi.css/umi.js path to "/css/umi.css" and "/js/umi.js"<br/>
    
4. profileUI<br/>
    run "yarn start" to start in dev mode<br/>
    run "npm run build" to build the final html/js/css<br/>
    copy dist/umi.css to oauthclientprofile\src\main\resources\static\css folder<br/>
    copy dist/umi.js to oauthclientprofile\src\main\resources\static\js folder<br/>
    copy dist/index.html to oauthclientprofile\src\main\resources\templates\profile.html<br/>
    modify profile.html, change umi.css/umi.js path to "/css/umi.css" and "/js/umi.js"<br/>
    
6. start oauthserver<br/>
    the URL is http://localhost:8080<br/>

7. start oauthclientprofile<br/>
    the URL is http://localhost:8500/profile<br/>

## How to simulate QR scan login:
1.  get mobile login token<br/>
    POST http://localhost:8080/mobile/login<br/>
    Content-Type: application/json<br/>
    BODY: <br/>
    {<br/>
      "userId":"jack.yu05@sap.com",<br/>
      "password":"123456"<br/>
    }<br/>

    The response is like below:<br/>
    {<br/>
        "status": "success",<br/>
        "errorMessage": null,<br/>
        "data": {<br/>
            "token": "d1438254-7865-427a-be00-85affd55f505"<br/>
        }<br/>
    }    <br/>
2.  simulate QR scan,<br/>
    in oauthserver console log, get below qrlogin URL:<br/>
    POST http://localhost:8080/qrLogin/scan?uuid=2f08c44c-84f4-4866-a5df-49bcc0e53131<br/>
    Content-Type: application/json<br/>
    BODY:<br/>
    {<br/>
      "userId":"jack.yu05@sap.com",<br/>
      "token":"433e721c-8d3f-498b-b7e0-de0719018f06"<br/>
    }<br/>
    
    