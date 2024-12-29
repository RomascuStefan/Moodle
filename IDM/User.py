from peewee import *
from datetime import datetime
import bcrypt

db = MySQLDatabase('academia_idm', user='root', password='parola', host='localhost', port=3308)

class User(Model):
    email = CharField(unique=True, column_name='email')
    password = CharField(column_name='parola')
    role = CharField(column_name='rol')
    salt = CharField(column_name='salt')  # Adăugăm salt-ul în baza de date

    class Meta:
        database = db
        table_name = 'Utilizatori'

    @staticmethod
    def hash_password(password, salt):
        # Folosim salt-ul generat pentru a hash-ui parola
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
        # Generăm salt-ul pe server folosind data și ora curentă
        now = datetime.now()
        return f"{now.hour:02}{now.minute:02}{now.second:02}"
