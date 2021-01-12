import axios from 'axios';

const USER_API_BASE_URL = "http://localhost:8080/api";

class MemberService{

    //로그인
    login(user){
        return axios.post(USER_API_BASE_URL+'/login',user);
    }
    
    //회원가입
    register(user){
        return axios.post(USER_API_BASE_URL+'/register',user);
    }
}

export default new MemberService();