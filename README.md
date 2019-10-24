# 제5회 국방오픈소스소프트웨어 캠프
<br/><br/>

# 소나기(소중한 나의 병영일기)  APP Release 1.0ver.

![메인](https://user-images.githubusercontent.com/54613588/67464197-e849b400-f67d-11e9-88f2-3590114565ce.PNG)


### Developer
 #### 조찬민,박민재
훈련병 시절부터 적어온 소나기가 핸드폰 실시 이후 존재감이 잊혀지고 있다...*****~~(설마 너도 소나기 안쓰고 있니??)~~*****
우리는 그 존재감을 다시 회복시키기 위해서 이 앱을 개발했다.<br />일기만 쓴다면 우리는 핸드폰에 내장되어 있는 켈린더앱을 사용할 것이다.
<br />하지만 "소나기" App은 사용자 중심의 InterFace기반으로 휴가, 훈련등의 계획관리도<br /> 쉽게 가능하고
        군 생활을 하는 사용자가 편리하도록 자신의 군생활에 관련된 정보를 제공해준다.

## App Functions

  1. 계획 관리
  
  2. 일일 일기
  
  3. 군 생활 카운터, 남은 휴가 일수 관리 

## Prerequisites
  SdkVersion 28(최소 16이상은 필수)
  
  Java Jdk 8.0
  
  AndroidStudio

  ### Front-End

   AndroidStudio
	
	
   ㄴ ~~에뮬레이터는 안드로이드 스튜디오를 사용하지 않고 지니모션을 사용하였습니다.~~ 
  
  
   개인적으로 안드로이드 에뮬보다 지니모션이 빨른거 같다.


 ## Back-End

   Django Restful API 
	
	
   ㄴ ~~장고를 이용해 안드로이드를 사용한 좋은 예제~~
  
 
## Extern Library 
  
  Calendar: MaterialCalendarView[Github](https://github.com/prolificinteractive/material-calendarview)
  
  
  
## File Manifest

	**LoadingActivity** xml : (activity_loading.xml)

 	 ㄴLoginActivity xml : (activity_login.xml)
 
       	 ㄴRegistrActivity xml : (activity_info_register.xml)
 
 	
 
 
 	**MainActivty(BottomNavigationView)**  xml: (acitivity_main.xml)
 	 ㄴFirstFragement (fragement_first) xml :activity_fist.xml
 	   ㄴMaterialCalendarView
 
 	 ㄴSecondFragement (fragment_second.xml)
 	   ㄴ 가로 listview (com.sonagi.android.myapplication/today_tab) xml :today_tv.xml
	   ㄴ 일정 listview (com.sonagi.android.myapplication/row) xml : row_chkbox.xml
	   ㄴ 추가 Dialog xml : add_diaog
 	 ㄴThirdFragment (fragment_third.xml)
	   ㄴInfoModifyActivity xml : (activity_info_modify.xml)
         ㄴAlarmReceiver, AlarmService
  
  **Login Acting**

   LoadingActivity에서 로그인에 대한 토큰 검사 후 없을 시 로그인 창
   토큰 존재시  MainActivty로 넘어가는 방식입니다.


## Installation Process
   저희 깃헙 저장소를 클론하시면 됩니다!. 
   안드로이드 클론 방법 [바로가기](https://webnautes.tistory.com/1175)
   
   
 ## Copyright / End User License
 
   
   
 ## Contact Information
  email: happyjarban@gmail.com or sobu0715@gmail.com
   
   
 ## Known Issues
  ~~None~~
 
 
## Troubleshooting
 위의 연락처에 메일을 주시면 답변해 드리겠습니다.

## Credit
  
  
## Change Log
 [2019.10.25]Relaese 1.0 version 
