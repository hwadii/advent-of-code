# six-digits
# within the range
# two adjacent digits are the same
# the digits never decrease


def format_input():
    '''Format input into a range'''
    it = input().split("-")
    return range(int(it[0]), int(it[1]))


def is_double(pd):
    '''Determine if there is same adjacent digits in pass'''
    cnt = 1
    cnts = []
    for i in range(1, len(pd)):
        if pd[i] == pd[i-1]:
            cnt += 1
        else:
            cnts.append(cnt)
            cnt = 1
    cnts.append(cnt)
    return 2 in cnts


def is_increasing(pd):
    '''Determine if digits in pass are increasing'''
    for i in range(1, len(pd)):
        if pd[i] < pd[i-1]:
            return False
    return True


def compute_passwords(rg):
    cnt = 0
    for passwd in rg:
        passwd = str(passwd)
        if is_double(passwd) and is_increasing(passwd):
            cnt += 1
    return cnt


rg = format_input()
print(compute_passwords(rg))
