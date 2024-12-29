import traceback
from asyncio import futures
import grpc
import auth_service_pb2
import auth_service_pb2_grpc
from User import User, db
from jwtManager import JWTManager
from concurrent import futures


class AuthService(auth_service_pb2_grpc.AuthServiceServicer):
    def RegisterUser(self, request, context):
        email = request.email
        password = request.password
        role = request.role+1 #diferenta de index in java daca trimit student are val 2, aici student are val 3

        if User.select().where(User.email == email).count() > 0:
            return auth_service_pb2.RegisterUserResponse(
                message="Email already exists",
                success=False
            )

        salt = User.generateSalt()
        hashed_password = User.hash_password(password, salt)

        try:
            new_user = User.create(email=email, password=hashed_password, role=role, salt=salt)
            return auth_service_pb2.RegisterUserResponse(
                message="User registered successfully",
                success=True,
            )
        except Exception as e:
            error_message = f"A fost o eroare la salvarea utilizatorului: {str(e)}\n{traceback.format_exc()}"
            print(error_message)
            return auth_service_pb2.RegisterUserResponse(
                message=f"Error: {str(e)}",
                success=False
            )

    def AuthenticateUser(self, request, context):
        email = request.email
        password = request.password

        try:
            user = User.get(User.email == email)

            if User.verify_password(email, password, user.password):
                token = JWTManager.generate_token(user.email, user.role)
                return auth_service_pb2.AuthenticateUserResponse(
                    token=token,
                    message="Authentication successful",
                    success=True
                )
            else:
                return auth_service_pb2.AuthenticateUserResponse(
                    token="",
                    message="Email or password wrong",
                    success=False
                )
        except User.DoesNotExist:
            return auth_service_pb2.AuthenticateUserResponse(
                token="",
                message="Email or password wrong",
                success=False
            )

    def GetUserDetails(self, request, context):
        token = request.token

        try:
            payload = JWTManager.verify_token(token)
            email = payload['email']
            role = payload['role']

            user = User.get(User.email == email)
            return auth_service_pb2.GetUserDetailsResponse(
                email=user.email,
                role=user.role,
                success=True,
                message="User details fetched successfully"
            )
        except Exception as e:
            return auth_service_pb2.GetUserDetailsResponse(
                email="",
                role="",
                success=False,
                message=str(e)
            )

    def VerifyToken(self, request, context):
        token = request.token
        try:
            payload = JWTManager.verify_token(token)
            return auth_service_pb2.VerifyTokenResponse(
                valid=True,
                message="Token is valid",
                role=payload['role']
            )
        except Exception as e:
            return auth_service_pb2.VerifyTokenResponse(
                valid=False,
                message=str(e)
            )


def serve():
    db.connect()

    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    auth_service_pb2_grpc.add_AuthServiceServicer_to_server(AuthService(), server)
    server.add_insecure_port('[::]:50051')
    print("Serverul gRPC rulează pe portul 50051...")
    server.start()
    server.wait_for_termination()


if __name__ == '__main__':
    serve()
