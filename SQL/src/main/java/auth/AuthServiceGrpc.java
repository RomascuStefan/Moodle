package auth;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: auth_service.proto")
public final class AuthServiceGrpc {

  private AuthServiceGrpc() {}

  public static final String SERVICE_NAME = "auth.AuthService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<auth.AuthServiceOuterClass.RegisterUserRequest,
      auth.AuthServiceOuterClass.RegisterUserResponse> getRegisterUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RegisterUser",
      requestType = auth.AuthServiceOuterClass.RegisterUserRequest.class,
      responseType = auth.AuthServiceOuterClass.RegisterUserResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<auth.AuthServiceOuterClass.RegisterUserRequest,
      auth.AuthServiceOuterClass.RegisterUserResponse> getRegisterUserMethod() {
    io.grpc.MethodDescriptor<auth.AuthServiceOuterClass.RegisterUserRequest, auth.AuthServiceOuterClass.RegisterUserResponse> getRegisterUserMethod;
    if ((getRegisterUserMethod = AuthServiceGrpc.getRegisterUserMethod) == null) {
      synchronized (AuthServiceGrpc.class) {
        if ((getRegisterUserMethod = AuthServiceGrpc.getRegisterUserMethod) == null) {
          AuthServiceGrpc.getRegisterUserMethod = getRegisterUserMethod = 
              io.grpc.MethodDescriptor.<auth.AuthServiceOuterClass.RegisterUserRequest, auth.AuthServiceOuterClass.RegisterUserResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "auth.AuthService", "RegisterUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  auth.AuthServiceOuterClass.RegisterUserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  auth.AuthServiceOuterClass.RegisterUserResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new AuthServiceMethodDescriptorSupplier("RegisterUser"))
                  .build();
          }
        }
     }
     return getRegisterUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<auth.AuthServiceOuterClass.AuthenticateUserRequest,
      auth.AuthServiceOuterClass.AuthenticateUserResponse> getAuthenticateUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AuthenticateUser",
      requestType = auth.AuthServiceOuterClass.AuthenticateUserRequest.class,
      responseType = auth.AuthServiceOuterClass.AuthenticateUserResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<auth.AuthServiceOuterClass.AuthenticateUserRequest,
      auth.AuthServiceOuterClass.AuthenticateUserResponse> getAuthenticateUserMethod() {
    io.grpc.MethodDescriptor<auth.AuthServiceOuterClass.AuthenticateUserRequest, auth.AuthServiceOuterClass.AuthenticateUserResponse> getAuthenticateUserMethod;
    if ((getAuthenticateUserMethod = AuthServiceGrpc.getAuthenticateUserMethod) == null) {
      synchronized (AuthServiceGrpc.class) {
        if ((getAuthenticateUserMethod = AuthServiceGrpc.getAuthenticateUserMethod) == null) {
          AuthServiceGrpc.getAuthenticateUserMethod = getAuthenticateUserMethod = 
              io.grpc.MethodDescriptor.<auth.AuthServiceOuterClass.AuthenticateUserRequest, auth.AuthServiceOuterClass.AuthenticateUserResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "auth.AuthService", "AuthenticateUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  auth.AuthServiceOuterClass.AuthenticateUserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  auth.AuthServiceOuterClass.AuthenticateUserResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new AuthServiceMethodDescriptorSupplier("AuthenticateUser"))
                  .build();
          }
        }
     }
     return getAuthenticateUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<auth.AuthServiceOuterClass.GetUserDetailsRequest,
      auth.AuthServiceOuterClass.GetUserDetailsResponse> getGetUserDetailsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetUserDetails",
      requestType = auth.AuthServiceOuterClass.GetUserDetailsRequest.class,
      responseType = auth.AuthServiceOuterClass.GetUserDetailsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<auth.AuthServiceOuterClass.GetUserDetailsRequest,
      auth.AuthServiceOuterClass.GetUserDetailsResponse> getGetUserDetailsMethod() {
    io.grpc.MethodDescriptor<auth.AuthServiceOuterClass.GetUserDetailsRequest, auth.AuthServiceOuterClass.GetUserDetailsResponse> getGetUserDetailsMethod;
    if ((getGetUserDetailsMethod = AuthServiceGrpc.getGetUserDetailsMethod) == null) {
      synchronized (AuthServiceGrpc.class) {
        if ((getGetUserDetailsMethod = AuthServiceGrpc.getGetUserDetailsMethod) == null) {
          AuthServiceGrpc.getGetUserDetailsMethod = getGetUserDetailsMethod = 
              io.grpc.MethodDescriptor.<auth.AuthServiceOuterClass.GetUserDetailsRequest, auth.AuthServiceOuterClass.GetUserDetailsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "auth.AuthService", "GetUserDetails"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  auth.AuthServiceOuterClass.GetUserDetailsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  auth.AuthServiceOuterClass.GetUserDetailsResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new AuthServiceMethodDescriptorSupplier("GetUserDetails"))
                  .build();
          }
        }
     }
     return getGetUserDetailsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<auth.AuthServiceOuterClass.VerifyTokenRequest,
      auth.AuthServiceOuterClass.VerifyTokenResponse> getVerifyTokenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "VerifyToken",
      requestType = auth.AuthServiceOuterClass.VerifyTokenRequest.class,
      responseType = auth.AuthServiceOuterClass.VerifyTokenResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<auth.AuthServiceOuterClass.VerifyTokenRequest,
      auth.AuthServiceOuterClass.VerifyTokenResponse> getVerifyTokenMethod() {
    io.grpc.MethodDescriptor<auth.AuthServiceOuterClass.VerifyTokenRequest, auth.AuthServiceOuterClass.VerifyTokenResponse> getVerifyTokenMethod;
    if ((getVerifyTokenMethod = AuthServiceGrpc.getVerifyTokenMethod) == null) {
      synchronized (AuthServiceGrpc.class) {
        if ((getVerifyTokenMethod = AuthServiceGrpc.getVerifyTokenMethod) == null) {
          AuthServiceGrpc.getVerifyTokenMethod = getVerifyTokenMethod = 
              io.grpc.MethodDescriptor.<auth.AuthServiceOuterClass.VerifyTokenRequest, auth.AuthServiceOuterClass.VerifyTokenResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "auth.AuthService", "VerifyToken"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  auth.AuthServiceOuterClass.VerifyTokenRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  auth.AuthServiceOuterClass.VerifyTokenResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new AuthServiceMethodDescriptorSupplier("VerifyToken"))
                  .build();
          }
        }
     }
     return getVerifyTokenMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AuthServiceStub newStub(io.grpc.Channel channel) {
    return new AuthServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AuthServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new AuthServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AuthServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new AuthServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class AuthServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void registerUser(auth.AuthServiceOuterClass.RegisterUserRequest request,
        io.grpc.stub.StreamObserver<auth.AuthServiceOuterClass.RegisterUserResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRegisterUserMethod(), responseObserver);
    }

    /**
     */
    public void authenticateUser(auth.AuthServiceOuterClass.AuthenticateUserRequest request,
        io.grpc.stub.StreamObserver<auth.AuthServiceOuterClass.AuthenticateUserResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAuthenticateUserMethod(), responseObserver);
    }

    /**
     */
    public void getUserDetails(auth.AuthServiceOuterClass.GetUserDetailsRequest request,
        io.grpc.stub.StreamObserver<auth.AuthServiceOuterClass.GetUserDetailsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetUserDetailsMethod(), responseObserver);
    }

    /**
     */
    public void verifyToken(auth.AuthServiceOuterClass.VerifyTokenRequest request,
        io.grpc.stub.StreamObserver<auth.AuthServiceOuterClass.VerifyTokenResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getVerifyTokenMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getRegisterUserMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                auth.AuthServiceOuterClass.RegisterUserRequest,
                auth.AuthServiceOuterClass.RegisterUserResponse>(
                  this, METHODID_REGISTER_USER)))
          .addMethod(
            getAuthenticateUserMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                auth.AuthServiceOuterClass.AuthenticateUserRequest,
                auth.AuthServiceOuterClass.AuthenticateUserResponse>(
                  this, METHODID_AUTHENTICATE_USER)))
          .addMethod(
            getGetUserDetailsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                auth.AuthServiceOuterClass.GetUserDetailsRequest,
                auth.AuthServiceOuterClass.GetUserDetailsResponse>(
                  this, METHODID_GET_USER_DETAILS)))
          .addMethod(
            getVerifyTokenMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                auth.AuthServiceOuterClass.VerifyTokenRequest,
                auth.AuthServiceOuterClass.VerifyTokenResponse>(
                  this, METHODID_VERIFY_TOKEN)))
          .build();
    }
  }

  /**
   */
  public static final class AuthServiceStub extends io.grpc.stub.AbstractStub<AuthServiceStub> {
    private AuthServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private AuthServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AuthServiceStub(channel, callOptions);
    }

    /**
     */
    public void registerUser(auth.AuthServiceOuterClass.RegisterUserRequest request,
        io.grpc.stub.StreamObserver<auth.AuthServiceOuterClass.RegisterUserResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRegisterUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void authenticateUser(auth.AuthServiceOuterClass.AuthenticateUserRequest request,
        io.grpc.stub.StreamObserver<auth.AuthServiceOuterClass.AuthenticateUserResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAuthenticateUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getUserDetails(auth.AuthServiceOuterClass.GetUserDetailsRequest request,
        io.grpc.stub.StreamObserver<auth.AuthServiceOuterClass.GetUserDetailsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetUserDetailsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void verifyToken(auth.AuthServiceOuterClass.VerifyTokenRequest request,
        io.grpc.stub.StreamObserver<auth.AuthServiceOuterClass.VerifyTokenResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getVerifyTokenMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class AuthServiceBlockingStub extends io.grpc.stub.AbstractStub<AuthServiceBlockingStub> {
    private AuthServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private AuthServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AuthServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public auth.AuthServiceOuterClass.RegisterUserResponse registerUser(auth.AuthServiceOuterClass.RegisterUserRequest request) {
      return blockingUnaryCall(
          getChannel(), getRegisterUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public auth.AuthServiceOuterClass.AuthenticateUserResponse authenticateUser(auth.AuthServiceOuterClass.AuthenticateUserRequest request) {
      return blockingUnaryCall(
          getChannel(), getAuthenticateUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public auth.AuthServiceOuterClass.GetUserDetailsResponse getUserDetails(auth.AuthServiceOuterClass.GetUserDetailsRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetUserDetailsMethod(), getCallOptions(), request);
    }

    /**
     */
    public auth.AuthServiceOuterClass.VerifyTokenResponse verifyToken(auth.AuthServiceOuterClass.VerifyTokenRequest request) {
      return blockingUnaryCall(
          getChannel(), getVerifyTokenMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class AuthServiceFutureStub extends io.grpc.stub.AbstractStub<AuthServiceFutureStub> {
    private AuthServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private AuthServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AuthServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<auth.AuthServiceOuterClass.RegisterUserResponse> registerUser(
        auth.AuthServiceOuterClass.RegisterUserRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRegisterUserMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<auth.AuthServiceOuterClass.AuthenticateUserResponse> authenticateUser(
        auth.AuthServiceOuterClass.AuthenticateUserRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAuthenticateUserMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<auth.AuthServiceOuterClass.GetUserDetailsResponse> getUserDetails(
        auth.AuthServiceOuterClass.GetUserDetailsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetUserDetailsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<auth.AuthServiceOuterClass.VerifyTokenResponse> verifyToken(
        auth.AuthServiceOuterClass.VerifyTokenRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getVerifyTokenMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REGISTER_USER = 0;
  private static final int METHODID_AUTHENTICATE_USER = 1;
  private static final int METHODID_GET_USER_DETAILS = 2;
  private static final int METHODID_VERIFY_TOKEN = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AuthServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(AuthServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REGISTER_USER:
          serviceImpl.registerUser((auth.AuthServiceOuterClass.RegisterUserRequest) request,
              (io.grpc.stub.StreamObserver<auth.AuthServiceOuterClass.RegisterUserResponse>) responseObserver);
          break;
        case METHODID_AUTHENTICATE_USER:
          serviceImpl.authenticateUser((auth.AuthServiceOuterClass.AuthenticateUserRequest) request,
              (io.grpc.stub.StreamObserver<auth.AuthServiceOuterClass.AuthenticateUserResponse>) responseObserver);
          break;
        case METHODID_GET_USER_DETAILS:
          serviceImpl.getUserDetails((auth.AuthServiceOuterClass.GetUserDetailsRequest) request,
              (io.grpc.stub.StreamObserver<auth.AuthServiceOuterClass.GetUserDetailsResponse>) responseObserver);
          break;
        case METHODID_VERIFY_TOKEN:
          serviceImpl.verifyToken((auth.AuthServiceOuterClass.VerifyTokenRequest) request,
              (io.grpc.stub.StreamObserver<auth.AuthServiceOuterClass.VerifyTokenResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class AuthServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AuthServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return auth.AuthServiceOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("AuthService");
    }
  }

  private static final class AuthServiceFileDescriptorSupplier
      extends AuthServiceBaseDescriptorSupplier {
    AuthServiceFileDescriptorSupplier() {}
  }

  private static final class AuthServiceMethodDescriptorSupplier
      extends AuthServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    AuthServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (AuthServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AuthServiceFileDescriptorSupplier())
              .addMethod(getRegisterUserMethod())
              .addMethod(getAuthenticateUserMethod())
              .addMethod(getGetUserDetailsMethod())
              .addMethod(getVerifyTokenMethod())
              .build();
        }
      }
    }
    return result;
  }
}
