#pragma once
#include "Layer.h"
#include <vector>

class Layer;
class Oxigen;
class Ozone;
class CarbonDioxide;

// SINGLETON TERVMINTA ALAPJÁN
class Atmosphere
{
	public:
		static Atmosphere* getInstance() {
			if (instance == nullptr) {
				instance = new Atmosphere;
				return instance;
			}
			return instance;
		};

		Layer* transform(Oxigen x, char w);
		Layer* transform(Ozone z, char w);
		Layer* transform(CarbonDioxide s, char w);

		std::vector<Layer*> getLayers() { return layers; }
		std::vector<Layer*> getTransformedLayers() { return transformedLayers; }
		int getNumberOfLayers() { return numberOfLayers; }
		std::string getWeathers() { return weathers; }

		void setNumberOfLayers(int n) { numberOfLayers = n; }
		void setWeathers(std::string w) { weathers = w; }
	private:
		int numberOfLayers;
		std::string weathers;
		std::vector<Layer*> layers;
		std::vector<Layer*> transformedLayers;
		static Atmosphere* instance;
		Atmosphere() {}
};
