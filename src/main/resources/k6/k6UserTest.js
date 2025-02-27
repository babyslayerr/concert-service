import http from 'k6/http';
import { sleep } from 'k6';
export const options = {
    // vus: 10,
    vus: 100,
    duration: '60s',
    // duration: '30s',
};
export default function () {

    let userId = Math.floor(Math.random() * 5)
    let uuid = getToken();
    // ~30000까지 무작위 충전
    let amount = Math.floor(Math.random() * 30000)

    checkStatus(uuid)

    charge(userId,amount,uuid)
    let chargeAmount = getAmount(userId,uuid)

    console.log(chargeAmount)
    sleep(1);

}

function getToken(){
    // 토큰 발급
    return http.post("http://localhost:8080/token").body;
}
function checkStatus(uuid){
    while(true){
        let response = JSON.parse(http.get(`http://localhost:8080/token/status?uuid=${uuid}`).body);
        console.log(response)
        if(response.status === 'active'){
            break;
        }
        // 5초마다 폴링
        sleep(5);
    }
}

function getAmount(userId,uuid){
    let params = {
        cookies: {
            "queue-uuid": uuid
        }
    };
    let response = http.get(`http://localhost:8080//payment/balance/${userId}`,params);

    return response.body
}

function charge(userId,amount,uuid){
    const url = 'http://localhost:8080/payment/charge';
    const payload = JSON.stringify({
        userId,
        amount
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
        cookies: {
            "queue-uuid": uuid
        }
    };

    http.post(url, payload, params)
}
