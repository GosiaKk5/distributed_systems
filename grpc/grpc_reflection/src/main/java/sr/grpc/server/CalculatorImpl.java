package sr.grpc.server;

import sr.grpc.gen.ArithmeticOpResult;
import sr.grpc.gen.CalculatorGrpc.CalculatorImplBase;
import sr.grpc.gen.MultiplicationOpResult;

public class CalculatorImpl extends CalculatorImplBase 
{
	@Override
	public void add(sr.grpc.gen.ArithmeticOpArguments request,
			io.grpc.stub.StreamObserver<ArithmeticOpResult> responseObserver)
	{
		System.out.println("addRequest (" + request.getArg1() + ", " + request.getArg2() +")");
		int val = request.getArg1() + request.getArg2();
		ArithmeticOpResult result = ArithmeticOpResult.newBuilder().setRes(val).build();
		if(request.getArg1() > 100 && request.getArg2() > 100) try { Thread.sleep(5000); } catch(InterruptedException ex) { }
		responseObserver.onNext(result);
		responseObserver.onCompleted();
	}

	@Override
	public void subtract(sr.grpc.gen.ArithmeticOpArguments request,
			io.grpc.stub.StreamObserver<ArithmeticOpResult> responseObserver)
	{
		System.out.println("subtractRequest (" + request.getArg1() + ", " + request.getArg2() +")");
		int val = request.getArg1() - request.getArg2();
		ArithmeticOpResult result = ArithmeticOpResult.newBuilder().setRes(val).build();
		responseObserver.onNext(result);
		responseObserver.onCompleted();
	}

	@Override
	public void multiple(sr.grpc.gen.MultiplicationOpArguments request,
						 io.grpc.stub.StreamObserver<MultiplicationOpResult> responseObserver){
		System.out.println("multipleRequest (" + request.getArgsList()+ ")");

		double val = 1;
		for(double arg: request.getArgsList()){
			val *= arg;
		}

		MultiplicationOpResult result = MultiplicationOpResult.newBuilder().setRes(val).build();
		responseObserver.onNext(result);
		responseObserver.onCompleted();
	}


}
