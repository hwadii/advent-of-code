def add(num1, num2):
    return num1 + num2

def multiply(num1, num2):
    return num1 * num2

def calculate(num1, num2, nums):
    nums[1] = num1
    nums[2] = num2
    idx = 0
    while nums[idx] != 99:
        num = nums[idx]
        val1 = nums[nums[idx + 1]]
        val2 = nums[nums[idx + 2]]
        idx1 = nums[idx + 1]
        idx3 = nums[idx + 3]
        if num == 1:
            nums[idx3] = add(val1, val2)
        elif num == 2:
            nums[idx3] = multiply(val1, val2)
        idx += 4
    return nums[0]

input_values = [int(num) for num in input().split(",")]
# part 1
print(calculate(12, 2, input_values[:]))

# part 2
GOAL = 19690720
for i in range(100):
    for j in range(100):
        if calculate(i, j, input_values[:]) == GOAL:
            print(100 * i + j)
            break
