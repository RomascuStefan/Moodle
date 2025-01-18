from peewee import *
from datetime import datetime
import bcrypt
from dotenv import load_dotenv
import os

load_dotenv()

db_name = os.getenv('IDM_DB_NAME')
db_user = os.getenv('IDM_DB_USER')
db_password = os.getenv('IDM_DB_PASSWORD')
db_host = os.getenv('IDM_DB_HOST')
db_port = int(os.getenv('IDM_DB_PORT'))

db = MySQLDatabase(db_name, user=db_user, password=db_password, host=db_host, port=db_port)

class User(Model):
    email = CharField(unique=True, column_name='email')
    password = CharField(column_name='parola')
    role = CharField(column_name='rol')
    salt = CharField(column_name='salt')

    class Meta:
        database = db
        table_name = 'Utilizatori'

    @staticmethod
    def hash_password(password, salt):
        concatenated_password = password + salt
        hashed_password = bcrypt.hashpw(concatenated_password.encode('utf-8'), bcrypt.gensalt()).decode('utf-8')
        return hashed_password

    @staticmethod
    def verify_password(email, password, hashed_password):
        salt = User.get(User.email == email).salt
        concatenated_password = password + salt
        return bcrypt.checkpw(concatenated_password.encode('utf-8'), hashed_password.encode('utf-8'))

    @staticmethod
    def generateSalt():
        now = datetime.now()
        return f"{now.hour:02}{now.minute:02}{now.second:02}"
