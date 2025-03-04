# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# NO CHECKED-IN PROTOBUF GENCODE
# source: auth_service.proto
# Protobuf Python Version: 5.28.1
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import runtime_version as _runtime_version
from google.protobuf import symbol_database as _symbol_database
from google.protobuf.internal import builder as _builder
_runtime_version.ValidateProtobufRuntimeVersion(
    _runtime_version.Domain.PUBLIC,
    5,
    28,
    1,
    '',
    'auth_service.proto'
)
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\x12\x61uth_service.proto\x12\x04\x61uth\"P\n\x13RegisterUserRequest\x12\r\n\x05\x65mail\x18\x01 \x01(\t\x12\x10\n\x08password\x18\x02 \x01(\t\x12\x18\n\x04role\x18\x03 \x01(\x0e\x32\n.auth.Role\"8\n\x14RegisterUserResponse\x12\x0f\n\x07message\x18\x01 \x01(\t\x12\x0f\n\x07success\x18\x02 \x01(\x08\":\n\x17\x41uthenticateUserRequest\x12\r\n\x05\x65mail\x18\x01 \x01(\t\x12\x10\n\x08password\x18\x02 \x01(\t\"K\n\x18\x41uthenticateUserResponse\x12\r\n\x05token\x18\x01 \x01(\t\x12\x0f\n\x07message\x18\x02 \x01(\t\x12\x0f\n\x07success\x18\x03 \x01(\x08\"&\n\x15GetUserDetailsRequest\x12\r\n\x05token\x18\x01 \x01(\t\"c\n\x16GetUserDetailsResponse\x12\r\n\x05\x65mail\x18\x01 \x01(\t\x12\x18\n\x04role\x18\x02 \x01(\x0e\x32\n.auth.Role\x12\x0f\n\x07success\x18\x03 \x01(\x08\x12\x0f\n\x07message\x18\x04 \x01(\t\"#\n\x12VerifyTokenRequest\x12\r\n\x05token\x18\x01 \x01(\t\"O\n\x13VerifyTokenResponse\x12\r\n\x05valid\x18\x01 \x01(\x08\x12\x0f\n\x07message\x18\x02 \x01(\t\x12\x18\n\x04role\x18\x03 \x01(\x0e\x32\n.auth.Role*@\n\x04Role\x12\t\n\x05\x61\x64min\x10\x00\x12\x0c\n\x08profesor\x10\x01\x12\x0b\n\x07student\x10\x02\x12\t\n\x05mongo\x10\x03\x12\x07\n\x03sql\x10\x04\x32\xb8\x02\n\x0b\x41uthService\x12\x45\n\x0cRegisterUser\x12\x19.auth.RegisterUserRequest\x1a\x1a.auth.RegisterUserResponse\x12Q\n\x10\x41uthenticateUser\x12\x1d.auth.AuthenticateUserRequest\x1a\x1e.auth.AuthenticateUserResponse\x12K\n\x0eGetUserDetails\x12\x1b.auth.GetUserDetailsRequest\x1a\x1c.auth.GetUserDetailsResponse\x12\x42\n\x0bVerifyToken\x12\x18.auth.VerifyTokenRequest\x1a\x19.auth.VerifyTokenResponseb\x06proto3')

_globals = globals()
_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, _globals)
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'auth_service_pb2', _globals)
if not _descriptor._USE_C_DESCRIPTORS:
  DESCRIPTOR._loaded_options = None
  _globals['_ROLE']._serialized_start=564
  _globals['_ROLE']._serialized_end=628
  _globals['_REGISTERUSERREQUEST']._serialized_start=28
  _globals['_REGISTERUSERREQUEST']._serialized_end=108
  _globals['_REGISTERUSERRESPONSE']._serialized_start=110
  _globals['_REGISTERUSERRESPONSE']._serialized_end=166
  _globals['_AUTHENTICATEUSERREQUEST']._serialized_start=168
  _globals['_AUTHENTICATEUSERREQUEST']._serialized_end=226
  _globals['_AUTHENTICATEUSERRESPONSE']._serialized_start=228
  _globals['_AUTHENTICATEUSERRESPONSE']._serialized_end=303
  _globals['_GETUSERDETAILSREQUEST']._serialized_start=305
  _globals['_GETUSERDETAILSREQUEST']._serialized_end=343
  _globals['_GETUSERDETAILSRESPONSE']._serialized_start=345
  _globals['_GETUSERDETAILSRESPONSE']._serialized_end=444
  _globals['_VERIFYTOKENREQUEST']._serialized_start=446
  _globals['_VERIFYTOKENREQUEST']._serialized_end=481
  _globals['_VERIFYTOKENRESPONSE']._serialized_start=483
  _globals['_VERIFYTOKENRESPONSE']._serialized_end=562
  _globals['_AUTHSERVICE']._serialized_start=631
  _globals['_AUTHSERVICE']._serialized_end=943
# @@protoc_insertion_point(module_scope)
