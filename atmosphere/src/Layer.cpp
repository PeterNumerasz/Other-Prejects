#include "Layer.h"

Layer* Oxigen::transform(char w) {
	Ozone* z;
	CarbonDioxide* s;
	switch (w)
	{
		case'n':
			//if (thickness * 0.05 < 0.5) return nullptr;
			z = new Ozone('z', thickness * 0.05);
			thickness *= 0.95;
			return z;
			break;
		case'z':
			//if (thickness * 0.5 < 0.5) return nullptr;
			z = new Ozone('z', thickness * 0.5);
			thickness *= 0.5;
			return z;
			break;
		case'm':
			//if (thickness * 0.1 < 0.5) return nullptr;
			s = new CarbonDioxide('s', thickness * 0.1);
			thickness *= 0.9;
			return s;
			break;
		default:
			return nullptr;
			break;
	}
}
Layer* Ozone::transform(char w) {
	Oxigen* x;
	switch (w)
	{

		case'm':
			//if (thickness * 0.05 < 0.5) return nullptr;
			x = new Oxigen('x', thickness * 0.05);
			thickness *= 0.95;
			return x;
			break;
		default:
			return nullptr;
			break;
	}
}
Layer* CarbonDioxide::transform(char w) {
	Oxigen* x;
	switch (w)
	{
		case'n':
			//if (thickness * 0.05 < 0.5) return nullptr;
			x = new Oxigen('x', thickness * 0.05);
			thickness *= 0.95;
			return x;
			break;
		default:
			return nullptr;
			break;
	}
}
