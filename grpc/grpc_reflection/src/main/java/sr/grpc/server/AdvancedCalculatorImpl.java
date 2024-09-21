package sr.grpc.server;

import io.grpc.stub.StreamObserver;
import sr.grpc.gen.ComplexArithmeticOpArguments;
import sr.grpc.gen.ComplexArithmeticOpResult;
import sr.grpc.gen.AdvancedCalculatorGrpc.AdvancedCalculatorImplBase;

import sr.grpc.gen.SeveralComplexArithmeticOpArguments;
import sr.grpc.gen.SeveralComplexArithmeticOpResults;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdvancedCalculatorImpl extends AdvancedCalculatorImplBase
{
	@Override
	public void complexOperation(ComplexArithmeticOpArguments request,
			StreamObserver<ComplexArithmeticOpResult> responseObserver)
	{
		System.out.println("multipleArgumentsRequest (" + request.getOptypeValue() + ", #" + request.getArgsCount() +")");
		
		if(request.getArgsCount() == 0) {
			System.out.println("No agruments");
		}
		
		double res = operationExecutor(request);
		
		ComplexArithmeticOpResult result = ComplexArithmeticOpResult.newBuilder().setRes(res).build();
		responseObserver.onNext(result);
		responseObserver.onCompleted();
	}

	@Override
	public void severalComplexOperations (SeveralComplexArithmeticOpArguments request,
										  StreamObserver<SeveralComplexArithmeticOpResults> responseObserver){

		List<ComplexArithmeticOpResult> severalComplexArithmeticOpResults = new ArrayList<>();

		for(ComplexArithmeticOpArguments opArguments: request.getManyOpArgumentsList()) {
			double res = operationExecutor(opArguments);

			ComplexArithmeticOpResult complexArithmeticOpResult = ComplexArithmeticOpResult.newBuilder().setRes(res).build();
			severalComplexArithmeticOpResults.add(complexArithmeticOpResult);
		}

		SeveralComplexArithmeticOpResults result = SeveralComplexArithmeticOpResults.newBuilder().
				addAllSeveralRes(severalComplexArithmeticOpResults).build();
		responseObserver.onNext(result);
		responseObserver.onCompleted();

	}

	private double operationExecutor(ComplexArithmeticOpArguments opArguments){
		double res = 0;
		switch (opArguments.getOptype()) {
			case SUM:
				for (Double d : opArguments.getArgsList()) res += d;
				break;
			case AVG:
				for (Double d : opArguments.getArgsList()) res += d;
				res /= opArguments.getArgsCount();
				break;
			case MIN:
				res = Collections.min(opArguments.getArgsList());
				break;
			case MAX:
				res = Collections.max(opArguments.getArgsList());
				break;
			case UNRECOGNIZED:
				break;
		}

		return res;
	}

}
