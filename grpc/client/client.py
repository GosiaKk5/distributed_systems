import subprocess

if __name__ == "__main__":

    while True:
        command = input("Enter operation: ")

        match(command):
            case "add":
                arg1 = input("Enter the first argument (arg1): ")
                arg2 = input("Enter the second argument (arg2): ")
                data = f'{{"arg1": {arg1}, "arg2": {arg2}}}'
                function = "calculator.Calculator/Add"

            case "subtract":
                arg1 = input("Enter the first argument (arg1): ")
                arg2 = input("Enter the second argument (arg2): ")
                data = f'{{"arg1": {arg1}, "arg2": {arg2}}}'
                function = "calculator.Calculator/Subtract"

            case "multiply":
                args = input("Enter the arguments: ")
                args = list(map(float, args.split()))
                data = f'{{"args": {args}}}'
                function = "calculator.Calculator/Multiple"

            case "advanced":
                operation = input("Enter operation SUM/AVG/MIN/MAX: ")
                args = input("Enter the arguments: ")
                args = list(map(float, args.split()))
                data = f'{{"optype": "{operation}", "args": {args}}}'
                function = "calculator.AdvancedCalculator/ComplexOperation"
            
            case "several advanced":
               
                data = f'{{"manyOpArguments": [{{"optype": "MIN", "args": [10.0, 5.1, 2.0]}}, {{"optype": "AVG", "args": [10.0, 5.1, 2.0]}}, {{"optype": "MAX", "args": [10.0, 5.1, 2.0]}}]}}'
                function = "calculator.AdvancedCalculator/SeveralComplexOperations"

            case _:
                print("Invalid operation")
                continue


        grpcurl_command = [
            "grpcurl",
            "-d", data,
            "-plaintext",
            "127.0.0.1:50051",
            function
        ]

        print(grpcurl_command)

        result = subprocess.run(grpcurl_command, capture_output=True, text=True)

        print("Standard Output:")
        print(result.stdout)

        print("Standard Error:")
        print(result.stderr)

