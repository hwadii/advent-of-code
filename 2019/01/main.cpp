#include <iostream>

int get_required_fuel(int mass)
{
  return mass / 3 - 2;
}

int get_required_fuel_rec(int fuel)
{
  int required = get_required_fuel(fuel);
  if (required <= 0)
    return 0;
  return required + get_required_fuel_rec(required);
}

int main(void) 
{
  int input;
  long int amountOfFuel = 0;
  while (std::cin >> input) {
    amountOfFuel += get_required_fuel_rec(input);
  }
  std::cout << amountOfFuel << std::endl;
  return 0;
}
