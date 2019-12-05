# find intersections
# calc distance between origin and closest intersection
line1 = input().split(",")
line2 = input().split(",")

def calc_points(moves):
    i, j = 0, 0
    steps = 0
    points = {}
    for move in moves:
        d = move[0]
        num = int(move[1:])
        for n in range(num):
            if d == "R":
                i += 1
            if d == "L":
                i -= 1
            if d == "U":
                j += 1
            if d == "D":
                j -= 1
            steps += 1
            points[(i, j)] = steps
    return points

def manhattan_distance(intersections):
    return [abs(x) + abs(y) for x, y in intersections]

def find_intersection(wire1, wire2):
    return set(wire1.keys()).intersection(set(wire2.keys()))

def least_steps(wire1, wire2, intersections):
    return [wire1[intersection] + wire2[intersection] for intersection in intersections]

wire1 = calc_points(line1)
wire2 = calc_points(line2)

intersection = find_intersection(wire1, wire2)
print(intersection)
print(min(manhattan_distance(intersection)))
print(min(least_steps(wire1, wire2, intersection)))


