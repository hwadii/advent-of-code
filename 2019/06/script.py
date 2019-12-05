orbitee = [] # the one who has an element which orbits around
orbiter = [] # the one who orbits around

def format_input():
    while True:
        try:
            data = input()
            data = data.split(")")
            orbitee.append(data[0])
            orbiter.append(data[1])
        except EOFError:
            break

def indirect_orbits():
    for i in range(len(orbitee)):
        for j in range(i, 0, -1):
            pass

def direct_orbits():
    return len(orbiter)

format_input()
print(orbitee)
print(orbiter)
print(f"{direct_orbits()}")
