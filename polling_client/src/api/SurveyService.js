import axios from "axios";

const USER_API_BASE_URL = "http://localhost:8080/api";

class SurveyService {
  //전체목록가져오기
  surveyList() {
    return axios.get(`${USER_API_BASE_URL}/survey`);
  }

  //내가만든 리스트
  madeList(user) {
    return axios.post(`${USER_API_BASE_URL}/myMaking`, user);
  }

  //내가 투표한 리스트
  voteList(user) {
    return axios.post(`${USER_API_BASE_URL}/myVoting`, user);
  }

  //설문 생성 요청
  makeSurvey(makeInfo) {
    return axios.post(`${USER_API_BASE_URL}/makeSurvey`, makeInfo);
  }
}

export default new SurveyService();
