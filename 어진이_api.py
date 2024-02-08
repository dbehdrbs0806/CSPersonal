from flask import Flask, request, json
import time
from SK_API import SK_API_CLASS

# falsk 객체 생성
app = Flask(__name__)
sac = SK_API_CLASS()

# 라우팅 생성
@app.route('/json_route', methods=['GET', 'POST'])
def route_json():
    print(request.method)
    if request.method == 'POST':
        try:
            point = json.loads(request.data)
            print(request.data)
            response = {
                'startPoint': point.get('start'),
                'endPoint': point.get('end')
            }

            startPoint = tuple(response["startPoint"])
            endPoint = tuple(response["endPoint"])
            print("startPoint: ", startPoint[0], startPoint[1], " / endPoint: ", endPoint[0], endPoint[1])

            route, route_totalDistance, route_totalTime, edit_route, edit_distance, edit_time = sac.apiWalkerStartEnd(startPoint, endPoint)

            print("Distance: ", route_totalDistance, "미터")
            print("Time: ", route_totalTime, "초")
            print("Edit Distance: ", edit_distance, "미터")
            print("Edit Time: ", edit_time, "초")

            result = {
                "route_totalDistance": route_totalDistance,
                "route_totalTime": route_totalTime,
                "edit_distance": edit_distance,
                "edit_time": edit_time,
                "route": route,
                "edit_route": edit_route
            }
            print(result)
        except:
            result = "POST request form: {'start':'(경도, 위도)', 'end':'(경도, 위도)'}"
            
    elif request.method == 'GET':
        try:
            start = request.args.get('start_point')
            end = request.args.get('end_point')

            # tuple 변환
            startPoint = tuple(map(float, start.replace('(', '').replace(')','').replace(' ','').split(',')))
            endPoint = tuple(map(float, end.replace('(', '').replace(')','').replace(' ','').split(',')))
            print("startPoint: ", startPoint[0], startPoint[1], " / endPoint: ", endPoint[0], endPoint[1])

            route, route_totalDistance, route_totalTime, edit_route, edit_distance, edit_time = sac.apiWalkerStartEnd(startPoint, endPoint)

            print("Distance: ", route_totalDistance, "미터")
            print("Time: ", route_totalTime, "초")
            print("Edit Distance: ", edit_distance, "미터")
            print("Edit Time: ", edit_time, "초")

            result = {
                "route_totalDistance": route_totalDistance,
                "route_totalTime": route_totalTime,
                "edit_distance": edit_distance,
                "edit_time": edit_time,
                "route": route,
                "edit_route": edit_route
            }
            print(result)
        except:
            result = "GET request form: start_point=(경도, 위도)&end_point=(경도, 위도)"
    else:
        result = "Request Type Error"
    return result

# 서버 작동
if __name__ == "__main__":
    app.run(host="220.69.209.124", port="8080")