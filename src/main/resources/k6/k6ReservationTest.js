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

    checkStatus(uuid)

    let scheduleId = getAvailableDates(uuid)

    let seatNo = getAvailableSeats(scheduleId,uuid)
    if(seatNo === undefined) return
    reserveSeat(userId,scheduleId,seatNo,uuid)

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
function getAvailableDates(uuid){
    let params = {
        cookies: {
            "queue-uuid": uuid
        }
    };

    let concertId = '1'
    let available_dates = JSON.parse(http.get(`http://localhost:8080/concert/available-dates/${concertId}`,params).body);

    return available_dates.content[14].id;
}
function getAvailableSeats(scheduleId,uuid){
    let params = {
        cookies: {
            "queue-uuid": uuid
        }
    };

    let response = http.get(`http://localhost:8080/concert/available-seats/${scheduleId}`,params);
    let available_seats = JSON.parse(response.body)
    let size = available_seats.length
    if (response.status !== 200 ) {
        console.log(available_seats.message)
        return;
    }

    // 랜덤 인덱스 선택 (0 ~ size-1 범위)
    let randomIndex = Math.floor(Math.random() * size);

    // 랜덤한 좌석 선택
    let seatNo = available_seats[randomIndex].seatNo;

    return seatNo
}

function reserveSeat(userId,concertScheduleId,seatNumber,uuid){
    const url = 'http://localhost:8080/concert/reserve';
    const payload = JSON.stringify({
        userId,
        concertScheduleId,
        seatNumber
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
