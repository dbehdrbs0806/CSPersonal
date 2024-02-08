import requests

class extra:
   url = "http://example.com/"
   headers = {"Authorization": "Bearer YourAccessToken"}
   data = {"key1": "value1", "key2": "value2"}
   response_data = requests.post(url, headers=headers, json=data).json()

   def __init__(self):                  # 생성자 부분

