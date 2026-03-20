package servicestubs;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Service contract operations 
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.70.0)",
    comments = "Source: NumbersServiceContract.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class NumbersServiceGrpc {

  private NumbersServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "grpcservice.NumbersService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<servicestubs.ProtoVoid,
      servicestubs.TextMessage> getIsAliveMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "isAlive",
      requestType = servicestubs.ProtoVoid.class,
      responseType = servicestubs.TextMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<servicestubs.ProtoVoid,
      servicestubs.TextMessage> getIsAliveMethod() {
    io.grpc.MethodDescriptor<servicestubs.ProtoVoid, servicestubs.TextMessage> getIsAliveMethod;
    if ((getIsAliveMethod = NumbersServiceGrpc.getIsAliveMethod) == null) {
      synchronized (NumbersServiceGrpc.class) {
        if ((getIsAliveMethod = NumbersServiceGrpc.getIsAliveMethod) == null) {
          NumbersServiceGrpc.getIsAliveMethod = getIsAliveMethod =
              io.grpc.MethodDescriptor.<servicestubs.ProtoVoid, servicestubs.TextMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "isAlive"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  servicestubs.ProtoVoid.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  servicestubs.TextMessage.getDefaultInstance()))
              .setSchemaDescriptor(new NumbersServiceMethodDescriptorSupplier("isAlive"))
              .build();
        }
      }
    }
    return getIsAliveMethod;
  }

  private static volatile io.grpc.MethodDescriptor<servicestubs.IntNumber,
      servicestubs.IntNumber> getGetEvenNumbersMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getEvenNumbers",
      requestType = servicestubs.IntNumber.class,
      responseType = servicestubs.IntNumber.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<servicestubs.IntNumber,
      servicestubs.IntNumber> getGetEvenNumbersMethod() {
    io.grpc.MethodDescriptor<servicestubs.IntNumber, servicestubs.IntNumber> getGetEvenNumbersMethod;
    if ((getGetEvenNumbersMethod = NumbersServiceGrpc.getGetEvenNumbersMethod) == null) {
      synchronized (NumbersServiceGrpc.class) {
        if ((getGetEvenNumbersMethod = NumbersServiceGrpc.getGetEvenNumbersMethod) == null) {
          NumbersServiceGrpc.getGetEvenNumbersMethod = getGetEvenNumbersMethod =
              io.grpc.MethodDescriptor.<servicestubs.IntNumber, servicestubs.IntNumber>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getEvenNumbers"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  servicestubs.IntNumber.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  servicestubs.IntNumber.getDefaultInstance()))
              .setSchemaDescriptor(new NumbersServiceMethodDescriptorSupplier("getEvenNumbers"))
              .build();
        }
      }
    }
    return getGetEvenNumbersMethod;
  }

  private static volatile io.grpc.MethodDescriptor<servicestubs.IntNumber,
      servicestubs.IntNumber> getAddSeqOfNumbersMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "addSeqOfNumbers",
      requestType = servicestubs.IntNumber.class,
      responseType = servicestubs.IntNumber.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<servicestubs.IntNumber,
      servicestubs.IntNumber> getAddSeqOfNumbersMethod() {
    io.grpc.MethodDescriptor<servicestubs.IntNumber, servicestubs.IntNumber> getAddSeqOfNumbersMethod;
    if ((getAddSeqOfNumbersMethod = NumbersServiceGrpc.getAddSeqOfNumbersMethod) == null) {
      synchronized (NumbersServiceGrpc.class) {
        if ((getAddSeqOfNumbersMethod = NumbersServiceGrpc.getAddSeqOfNumbersMethod) == null) {
          NumbersServiceGrpc.getAddSeqOfNumbersMethod = getAddSeqOfNumbersMethod =
              io.grpc.MethodDescriptor.<servicestubs.IntNumber, servicestubs.IntNumber>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "addSeqOfNumbers"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  servicestubs.IntNumber.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  servicestubs.IntNumber.getDefaultInstance()))
              .setSchemaDescriptor(new NumbersServiceMethodDescriptorSupplier("addSeqOfNumbers"))
              .build();
        }
      }
    }
    return getAddSeqOfNumbersMethod;
  }

  private static volatile io.grpc.MethodDescriptor<servicestubs.AddOperands,
      servicestubs.AddResult> getMultipleAddMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "multipleAdd",
      requestType = servicestubs.AddOperands.class,
      responseType = servicestubs.AddResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<servicestubs.AddOperands,
      servicestubs.AddResult> getMultipleAddMethod() {
    io.grpc.MethodDescriptor<servicestubs.AddOperands, servicestubs.AddResult> getMultipleAddMethod;
    if ((getMultipleAddMethod = NumbersServiceGrpc.getMultipleAddMethod) == null) {
      synchronized (NumbersServiceGrpc.class) {
        if ((getMultipleAddMethod = NumbersServiceGrpc.getMultipleAddMethod) == null) {
          NumbersServiceGrpc.getMultipleAddMethod = getMultipleAddMethod =
              io.grpc.MethodDescriptor.<servicestubs.AddOperands, servicestubs.AddResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "multipleAdd"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  servicestubs.AddOperands.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  servicestubs.AddResult.getDefaultInstance()))
              .setSchemaDescriptor(new NumbersServiceMethodDescriptorSupplier("multipleAdd"))
              .build();
        }
      }
    }
    return getMultipleAddMethod;
  }

  private static volatile io.grpc.MethodDescriptor<servicestubs.IntervalNumbers,
      servicestubs.IntNumber> getFindPrimesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "findPrimes",
      requestType = servicestubs.IntervalNumbers.class,
      responseType = servicestubs.IntNumber.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<servicestubs.IntervalNumbers,
      servicestubs.IntNumber> getFindPrimesMethod() {
    io.grpc.MethodDescriptor<servicestubs.IntervalNumbers, servicestubs.IntNumber> getFindPrimesMethod;
    if ((getFindPrimesMethod = NumbersServiceGrpc.getFindPrimesMethod) == null) {
      synchronized (NumbersServiceGrpc.class) {
        if ((getFindPrimesMethod = NumbersServiceGrpc.getFindPrimesMethod) == null) {
          NumbersServiceGrpc.getFindPrimesMethod = getFindPrimesMethod =
              io.grpc.MethodDescriptor.<servicestubs.IntervalNumbers, servicestubs.IntNumber>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "findPrimes"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  servicestubs.IntervalNumbers.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  servicestubs.IntNumber.getDefaultInstance()))
              .setSchemaDescriptor(new NumbersServiceMethodDescriptorSupplier("findPrimes"))
              .build();
        }
      }
    }
    return getFindPrimesMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static NumbersServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NumbersServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NumbersServiceStub>() {
        @java.lang.Override
        public NumbersServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NumbersServiceStub(channel, callOptions);
        }
      };
    return NumbersServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static NumbersServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NumbersServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NumbersServiceBlockingV2Stub>() {
        @java.lang.Override
        public NumbersServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NumbersServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return NumbersServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static NumbersServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NumbersServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NumbersServiceBlockingStub>() {
        @java.lang.Override
        public NumbersServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NumbersServiceBlockingStub(channel, callOptions);
        }
      };
    return NumbersServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static NumbersServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NumbersServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NumbersServiceFutureStub>() {
        @java.lang.Override
        public NumbersServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NumbersServiceFutureStub(channel, callOptions);
        }
      };
    return NumbersServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * Service contract operations 
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * ping server for testing service availability
     * </pre>
     */
    default void isAlive(servicestubs.ProtoVoid request,
        io.grpc.stub.StreamObserver<servicestubs.TextMessage> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getIsAliveMethod(), responseObserver);
    }

    /**
     * <pre>
     * get first N even numbers 2,...,K
     * </pre>
     */
    default void getEvenNumbers(servicestubs.IntNumber request,
        io.grpc.stub.StreamObserver<servicestubs.IntNumber> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetEvenNumbersMethod(), responseObserver);
    }

    /**
     * <pre>
     * add a sequence of numbers, ex: 1,2,10,5 = 18
     * </pre>
     */
    default io.grpc.stub.StreamObserver<servicestubs.IntNumber> addSeqOfNumbers(
        io.grpc.stub.StreamObserver<servicestubs.IntNumber> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getAddSeqOfNumbersMethod(), responseObserver);
    }

    /**
     * <pre>
     * multiple add operations using a bidirectional stream
     * </pre>
     */
    default io.grpc.stub.StreamObserver<servicestubs.AddOperands> multipleAdd(
        io.grpc.stub.StreamObserver<servicestubs.AddResult> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getMultipleAddMethod(), responseObserver);
    }

    /**
     * <pre>
     * find the prime numbers in IntervalNumbers
     * </pre>
     */
    default void findPrimes(servicestubs.IntervalNumbers request,
        io.grpc.stub.StreamObserver<servicestubs.IntNumber> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getFindPrimesMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service NumbersService.
   * <pre>
   * Service contract operations 
   * </pre>
   */
  public static abstract class NumbersServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return NumbersServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service NumbersService.
   * <pre>
   * Service contract operations 
   * </pre>
   */
  public static final class NumbersServiceStub
      extends io.grpc.stub.AbstractAsyncStub<NumbersServiceStub> {
    private NumbersServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NumbersServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NumbersServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * ping server for testing service availability
     * </pre>
     */
    public void isAlive(servicestubs.ProtoVoid request,
        io.grpc.stub.StreamObserver<servicestubs.TextMessage> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getIsAliveMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * get first N even numbers 2,...,K
     * </pre>
     */
    public void getEvenNumbers(servicestubs.IntNumber request,
        io.grpc.stub.StreamObserver<servicestubs.IntNumber> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getGetEvenNumbersMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * add a sequence of numbers, ex: 1,2,10,5 = 18
     * </pre>
     */
    public io.grpc.stub.StreamObserver<servicestubs.IntNumber> addSeqOfNumbers(
        io.grpc.stub.StreamObserver<servicestubs.IntNumber> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncClientStreamingCall(
          getChannel().newCall(getAddSeqOfNumbersMethod(), getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     * multiple add operations using a bidirectional stream
     * </pre>
     */
    public io.grpc.stub.StreamObserver<servicestubs.AddOperands> multipleAdd(
        io.grpc.stub.StreamObserver<servicestubs.AddResult> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getMultipleAddMethod(), getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     * find the prime numbers in IntervalNumbers
     * </pre>
     */
    public void findPrimes(servicestubs.IntervalNumbers request,
        io.grpc.stub.StreamObserver<servicestubs.IntNumber> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getFindPrimesMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service NumbersService.
   * <pre>
   * Service contract operations 
   * </pre>
   */
  public static final class NumbersServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<NumbersServiceBlockingV2Stub> {
    private NumbersServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NumbersServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NumbersServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     * <pre>
     * ping server for testing service availability
     * </pre>
     */
    public servicestubs.TextMessage isAlive(servicestubs.ProtoVoid request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getIsAliveMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * get first N even numbers 2,...,K
     * </pre>
     */
    @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/10918")
    public io.grpc.stub.BlockingClientCall<?, servicestubs.IntNumber>
        getEvenNumbers(servicestubs.IntNumber request) {
      return io.grpc.stub.ClientCalls.blockingV2ServerStreamingCall(
          getChannel(), getGetEvenNumbersMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * add a sequence of numbers, ex: 1,2,10,5 = 18
     * </pre>
     */
    @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/10918")
    public io.grpc.stub.BlockingClientCall<servicestubs.IntNumber, servicestubs.IntNumber>
        addSeqOfNumbers() {
      return io.grpc.stub.ClientCalls.blockingClientStreamingCall(
          getChannel(), getAddSeqOfNumbersMethod(), getCallOptions());
    }

    /**
     * <pre>
     * multiple add operations using a bidirectional stream
     * </pre>
     */
    @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/10918")
    public io.grpc.stub.BlockingClientCall<servicestubs.AddOperands, servicestubs.AddResult>
        multipleAdd() {
      return io.grpc.stub.ClientCalls.blockingBidiStreamingCall(
          getChannel(), getMultipleAddMethod(), getCallOptions());
    }

    /**
     * <pre>
     * find the prime numbers in IntervalNumbers
     * </pre>
     */
    @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/10918")
    public io.grpc.stub.BlockingClientCall<?, servicestubs.IntNumber>
        findPrimes(servicestubs.IntervalNumbers request) {
      return io.grpc.stub.ClientCalls.blockingV2ServerStreamingCall(
          getChannel(), getFindPrimesMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service NumbersService.
   * <pre>
   * Service contract operations 
   * </pre>
   */
  public static final class NumbersServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<NumbersServiceBlockingStub> {
    private NumbersServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NumbersServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NumbersServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * ping server for testing service availability
     * </pre>
     */
    public servicestubs.TextMessage isAlive(servicestubs.ProtoVoid request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getIsAliveMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * get first N even numbers 2,...,K
     * </pre>
     */
    public java.util.Iterator<servicestubs.IntNumber> getEvenNumbers(
        servicestubs.IntNumber request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getGetEvenNumbersMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * find the prime numbers in IntervalNumbers
     * </pre>
     */
    public java.util.Iterator<servicestubs.IntNumber> findPrimes(
        servicestubs.IntervalNumbers request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getFindPrimesMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service NumbersService.
   * <pre>
   * Service contract operations 
   * </pre>
   */
  public static final class NumbersServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<NumbersServiceFutureStub> {
    private NumbersServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NumbersServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NumbersServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * ping server for testing service availability
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<servicestubs.TextMessage> isAlive(
        servicestubs.ProtoVoid request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getIsAliveMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_IS_ALIVE = 0;
  private static final int METHODID_GET_EVEN_NUMBERS = 1;
  private static final int METHODID_FIND_PRIMES = 2;
  private static final int METHODID_ADD_SEQ_OF_NUMBERS = 3;
  private static final int METHODID_MULTIPLE_ADD = 4;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_IS_ALIVE:
          serviceImpl.isAlive((servicestubs.ProtoVoid) request,
              (io.grpc.stub.StreamObserver<servicestubs.TextMessage>) responseObserver);
          break;
        case METHODID_GET_EVEN_NUMBERS:
          serviceImpl.getEvenNumbers((servicestubs.IntNumber) request,
              (io.grpc.stub.StreamObserver<servicestubs.IntNumber>) responseObserver);
          break;
        case METHODID_FIND_PRIMES:
          serviceImpl.findPrimes((servicestubs.IntervalNumbers) request,
              (io.grpc.stub.StreamObserver<servicestubs.IntNumber>) responseObserver);
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
        case METHODID_ADD_SEQ_OF_NUMBERS:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.addSeqOfNumbers(
              (io.grpc.stub.StreamObserver<servicestubs.IntNumber>) responseObserver);
        case METHODID_MULTIPLE_ADD:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.multipleAdd(
              (io.grpc.stub.StreamObserver<servicestubs.AddResult>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getIsAliveMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              servicestubs.ProtoVoid,
              servicestubs.TextMessage>(
                service, METHODID_IS_ALIVE)))
        .addMethod(
          getGetEvenNumbersMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              servicestubs.IntNumber,
              servicestubs.IntNumber>(
                service, METHODID_GET_EVEN_NUMBERS)))
        .addMethod(
          getAddSeqOfNumbersMethod(),
          io.grpc.stub.ServerCalls.asyncClientStreamingCall(
            new MethodHandlers<
              servicestubs.IntNumber,
              servicestubs.IntNumber>(
                service, METHODID_ADD_SEQ_OF_NUMBERS)))
        .addMethod(
          getMultipleAddMethod(),
          io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
            new MethodHandlers<
              servicestubs.AddOperands,
              servicestubs.AddResult>(
                service, METHODID_MULTIPLE_ADD)))
        .addMethod(
          getFindPrimesMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              servicestubs.IntervalNumbers,
              servicestubs.IntNumber>(
                service, METHODID_FIND_PRIMES)))
        .build();
  }

  private static abstract class NumbersServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    NumbersServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return servicestubs.NumbersServiceContract.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("NumbersService");
    }
  }

  private static final class NumbersServiceFileDescriptorSupplier
      extends NumbersServiceBaseDescriptorSupplier {
    NumbersServiceFileDescriptorSupplier() {}
  }

  private static final class NumbersServiceMethodDescriptorSupplier
      extends NumbersServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    NumbersServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (NumbersServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new NumbersServiceFileDescriptorSupplier())
              .addMethod(getIsAliveMethod())
              .addMethod(getGetEvenNumbersMethod())
              .addMethod(getAddSeqOfNumbersMethod())
              .addMethod(getMultipleAddMethod())
              .addMethod(getFindPrimesMethod())
              .build();
        }
      }
    }
    return result;
  }
}
