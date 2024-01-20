# 1o1tube

## 📝프로젝트 설명
팀원들의 공통 관심사인 리그오브레전드 게임에서 따와 101(LoL)조로 팀명을 지었고, 이번 과제가 "나만의 미디어 앱" 프로젝트이기에

Retrofit과 YouTube API를 이용하여, 리그오브레전드와 관련된 영상을 찾거나, 즐겨찾기에 등록하는 앱을 제작 하려고 한다.

## ⏰개발 기간
23.09.25(월) ~ 23.10.05(목) 18:00

23.10.06(금) 발표일

## ⚙️주요 기능
* HomeFragment
  * Most Popular Videos 목록
    * YouTube의 현재 Trend 비디오 목록을 출력
  * 비디오 카테고리 조회
    * YouTube에서 제공하는 다양한 비디오 카테고리를 조회.
  * 카테고리 별 비디오 목록 조회
    * 특정 카테고리에 속하는 인기 비디오 목록을 조회
  * Category Channels 목록
    * 특정 카테고리에 속하는 비디오의 각 채널들의 정보를 조회.
* ShortsFragment
  * 리그오브레전드 게임 관련 숏츠 영상 재생 페이지. 
* DetailFragment
  * HomeFragment와 SearchFragment에서 비디오 아이템 선택시, 비디오의 상세정보 제공
  * Like 버튼 클릭시, unLike 버튼으로 변경되며 해당 비디오 정보가 내부에 저장됨.
  * 저장된 비디오는 MyVideoFragment에서 확인 가능
* SearchFragment
  * 사용자가 원하는 비디오를 쉽게 검색하고 결과를 빠르게 확인할 수 있는 기능을 제공
  * 검색 쿼리 기반으로 비디오 정보를 가져옴
  * 세분화된 포지션 별로 영상 필터 가능  
* MyVideoFragment
  * 사용자의 개인 정보 및 사용자가 ‘좋아요’를 누른 비디오 목록을 보여주는 기능 제공
  * 사용자의 프로필 사진, 이름 등의 개인 정보를 상단에 표시
  * ‘좋아요’를 누른 비디오 목록은 **`RecyclerView`**를 사용해 아래쪽에 목록 형태로 출력됨
  * "좋아요" 버튼을 통해 추가된 비디오는 내부 (예: Room or SharedPreference)에  저장되며, 이 정보를 가져와서 표시.   

## 🎉팀원 및 역할 분담
<table>
  <tbody>
    <tr>
      <td align="center"><a href="https://github.com/choco5732"><img src="https://avatars.githubusercontent.com/u/81561579?v=4" width="100px;"><br /><sub><b>FE 팀장 : 장재용</b></sub></a><br /></a></td>
      <td align="center"><a href="https://github.com/hosiker"><img src="https://avatars.githubusercontent.com/u/139095490?v=4" width="100px;"><br /><sub><b>FE 팀원 : 이호식</b></sub></a><br /></a></td>
      <td align="center"><a href="https://github.com/kt2790"><img src="https://avatars.githubusercontent.com/u/138543028?v=4" width="100px;"><br /><sub><b>FE 팀원 : 배근태</b></sub></a><br /></a></td>
      <td align="center"><a href="https://github.com/AJH1346"><img src="https://avatars.githubusercontent.com/u/139087984?v=4" width="100px;"><br /><sub><b>FE 팀원 : 안주환</b></sub></a><br /></a></td>
     <tr/>
  </tbody>
</table>

1.장재용
* HomeFragment 담당하여 개발 진행
* 팀장 역할 수행

2.이호식
* SearchFragment 담당하여 개발 진행
* 발표 진행

3.배근태
* ShortsFragment 담당하여 개발 진행
* MyVideoFragment 담당하여 개발 진행
* 발표자료 준비

4.안주환
* DetailFragment 담당하여 개발 진행
* Readme 작성

## 📠팀 노션
https://teamsparta.notion.site/1o1-2d651cb16c0646fc958ec295e36f3c8e


## 💻기술스택
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"><img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"><img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">

<img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=white"><img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=Android&logoColor=white">
