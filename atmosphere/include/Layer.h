#pragma once
#include <iostream>
#include "Atmosphere.h"

class Atmosphere;

class Layer
{
	protected:
		Layer(char ty, double th) : type(ty), thickness(th) {}
		char type;
		double thickness;
		bool exists = true;
	public:
		virtual ~Layer() {}
		virtual Layer* transform(char w) = 0;
		void setLayerThickness(double d) { thickness = d; }
		char layerType() { return type; }
		double layerThickness() { return thickness; }
		bool doesExist() { return exists; }
		void destroy() { exists = false; }
		friend std::ostream& operator<<(std::ostream& os, const Layer& l)
		{
			os << "Layer type: " << l.type << ", layer thickness: " << l.thickness;
			return os;
		}
};

class Oxigen : public Layer {
	public:
		Oxigen(char ty, double th) : Layer(ty,th) {}
		Layer* transform(char w) override;
};

class Ozone : public Layer {
	public:
		Ozone(char ty, double th) : Layer(ty, th) {}
		Layer* transform(char w) override;
};

class CarbonDioxide : public Layer {
	public:
		CarbonDioxide(char ty, double th) : Layer(ty, th) {}
		Layer* transform(char w) override;
};
