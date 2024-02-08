from flask import Flask, request, json, jsonify
import base64

# 플라스크 라이브러리에서 request, jsonify의 모듈을 가져옴
# request는 reset의 get, post, put, delete를 사용하는 모듈 
#json 모듈의 함수는 json형태의 객체를 변환 시 사용 / jsonify는 Flask에서 JSON응답을 생성 시 사용


app = Flask(__name__)

@app.route('/post_json', methods=['POST'])                                  # post형식의 json 데이터를 받는 함수
def post_json():
    try:
        json_data = json.loads(request.data)                               # json_data: post형식으로 받은 객체(사진 정보)를 json.load해서 변수에 저장
        print(json_data)                                                   # json_data: 받은 객체를 임의로 출력해보는 테스트
    except json.JSONDecoderError:                                          # 만약 Json형태로 오지않고 base_64 형태로 데이터가 왔을경우 처리하는 형태
        base64_data = base64.b64decode(request.data)
        json_data = json.loads(base64_data.decode('utf-8'))
        print(json_data)
    return json_data                                                       # 이 부분이 핸드폰에서 받는 내용의 함수
 
def returnApiJson(self, url:str, headers:dict, data:dict) -> str:
        '''API POST 통신 후 응답을 Json으로 반환하는 함수
        @param url -> POST 통신 주소 값
        @param headers -> POST 통신 헤더 값
        @param data -> POST 통신 데이터 값

        @return Json 형식인 POST의 응답
        '''
        print(data)
        post_data = request.post(url, headers=headers, json=data).json()

        return post_data

if __name__ == "__main__":                                                 # main에 해당하는 부분으로 app의 내용을 실행
    app.run(host="220.69.209.124", port="8080")
    