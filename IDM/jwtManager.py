import jwt
import datetime
from typing import Dict

SECRET_KEY = "cheieSeCrEtA__semnatura_VALidArE"


class JWTManager:
    @staticmethod
    def generate_token(email: str, role: str) -> str:
        expiration_time = datetime.datetime.utcnow() + datetime.timedelta(hours=8)

        payload = {
            'email': email,
            'role': role,
            'exp': expiration_time
        }

        token = jwt.encode(payload, SECRET_KEY, algorithm="HS256")
        return token

    @staticmethod
    def verify_token(token: str) -> Dict:
        try:
            payload = jwt.decode(token, SECRET_KEY, algorithms=["HS256"])
            return payload
        except jwt.ExpiredSignatureError:
            raise Exception("Token has expired")
        except jwt.InvalidTokenError:
            raise Exception("Invalid token")
