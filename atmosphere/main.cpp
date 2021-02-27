#include <iostream>
#include <fstream>
#include "Atmosphere.h"

Atmosphere* Atmosphere::instance = nullptr;
Atmosphere* atmosphere = Atmosphere::getInstance();

enum ERRORS{OPEN_ERROR, EMPTY_FILE};

void create(const std::string& filename, std::vector<Layer*>& Layers, int& numberOfLayers, std::string& weather);
void testData(int n, std::string w, std::vector<Layer*> l);
void testNewData(std::vector<Layer*> l, int n);
void transformLayers(int n, char w, std::vector<Layer*>& oldLayers, std::vector<Layer*>& newLayers);
bool linSearch(std::vector<Layer*>& layers, Layer* l, int& n, int j);
void ascendLayers(std::vector<Layer*>& oldLayers, std::vector<Layer*>& newLayers, int& n, bool& l);
void step(int& n, std::string w, std::vector<Layer*>& originals, std::vector<Layer*>& oldLayers, std::vector<Layer*>& newLayers, bool &l, int& dayNum);
bool originalLayersAllExist(std::vector<Layer*> l);

int main()
{

    std::vector<Layer*> layers = atmosphere->getLayers();
    std::vector<Layer*> transformedLayers = atmosphere->getTransformedLayers();
    int n;
    std::string w = "";
    bool over = false;
    try
    {
        //Preparations for work with the data from the file
        create("input.txt", layers, n, w);
    }
    catch (ERRORS ex)
    {
        if (ex == OPEN_ERROR) {
            std::cout << "\nError while opening the given file!\n";
        }
        if (ex == EMPTY_FILE) {
            std::cout << "\nThe given file was empty!\n";
        }
    }
    std::vector<Layer*> originals = layers;
    atmosphere->setNumberOfLayers(n);
    atmosphere->setWeathers(w);
    n = atmosphere->getNumberOfLayers();
    w = atmosphere->getWeathers();
    testData(n, w, layers);
    int dayNum = 1;
    while (!over) {
        //std::cout << "#   # ##### #     # #####    ##### ##### #   # #   # #### \n";
        //std::cout << "##  # #      #   #    #      #   # #   # #   # ##  # #   #\n";
        //std::cout << "# # # ###      #      #      ##### #   # #   # # # # #   #\n";
        //std::cout << "#  ## #      #   #    #      #  #  #   # #   # #  ## #   #\n";
        //std::cout << "#   # ##### #     #   #      #   # ##### ##### #   # #### \n";
        step(n, w, originals, layers, transformedLayers, over, dayNum);
    }
    std::cout << "\n/ * - F I N A L   S T A T E - * \\\n\n";
    testNewData(layers, n);
    //std::cout << "Hello World!\n";
}

void create(const std::string& filename, std::vector<Layer*>& Layers, int& numberOfLayers, std::string& weather) {
    std::ifstream f(filename);
    if (f.fail()) {
        throw OPEN_ERROR;
    }
    if (f.peek() == EOF) {
        throw EMPTY_FILE;
    }
    int n;
    f >> n;
    numberOfLayers = n;
    Layers.resize(n);
    for (int i = 0; i < n; i++) {
        char c; double d;
        f >> c >> d;
        switch (c)
        {
        case 'x':
            Layers[i] = new Oxigen(c, d);
            break;
        case 'z':
            Layers[i] = new Ozone(c, d);
            break;
        case 's':
            Layers[i] = new CarbonDioxide(c, d);
            break;
        }
    }
    std::string w;
    f >> w;
    weather = w;
}
void testData(int n, std::string w, std::vector<Layer*> l) {
    std::cout << "The number of layers read from file is: " << n << "\n";
    std::cout << "The layers in order: \n";
    for (unsigned int i = 0; i < l.size(); i++) {
        std::cout << i + 1 << ". " << *(l[i]) << "\n";
    }
    std::cout << "And the series of weather are: \n";
    for (unsigned int i = 0; i < w.length(); i++) {
        std::cout << i + 1 << ". " << w[i] << "\n";
    }
}
void testNewData(std::vector<Layer*> l, int n) {
    //std::cout << "Number of layers: " << n << "\n";
    std::cout << "The layers in order: \n";
    for (unsigned int i = 0; i < l.size(); i++) {
        //if (l[i] != nullptr)
            std::cout << i + 1 << ". " << *(l[i]) << "\n";
        //else std::cout << "This was nullpointer : #" << i + 1 << "\n";
    }
}
void transformLayers(int n, char w, std::vector<Layer*>& oldLayers, std::vector<Layer*>& newLayers) {
    newLayers.clear();
    newLayers.resize(0);
    for (unsigned int i = 0; i < oldLayers.size(); i++) {
        //std::cout << "* * * We are now transforming the layer '" << *(oldLayers[i]) << "' * * *\n";
        //Layer* l;
        //if ((oldLayers[i]->transform(w))->layerThickness() > 0.5) {
        Layer* l = oldLayers[i]->transform(w);
        //}
        //if (l != nullptr) {
            //std::cout << "* * * Ater transforming we got '" << *(l) << "' and we are left with this layer '" << *(oldLayers[i]) << "' * * *\n\n";
            //if (l->layerThickness() < 0.5) {
            //    oldLayers[i]->destroy();
            //    //oldLayers.erase(oldLayers.begin() + i);
            //}
        //}
        //else std::cout << "* * * After transforming the layer '" << *(oldLayers[i]) << "' did not change * * *\n\n";
        //if (l != nullptr) {
        //if (l != nullptr && l->layerThickness() > 0.5) {
            newLayers.push_back(l);
        //}
        //}
    }
}
bool linSearch(std::vector<Layer*>& layers, Layer* l, int& n, int j) {
    bool found = false;
    //n = -1;
    if (j == 0) j = 1;
    for (unsigned int i = j; !found && i < layers.size() - 1; i++) {
        if (layers[i] != nullptr) {
            if (layers[i]->layerType() == l->layerType()) {
                n = i;
                found = true;
                break;
                //std::cout << "\n* * * I have found '" << l->layerType() << "' layer in the old layers at position #" << i + 1 << " * * *\n";
            }
        }
    }
    //if (!found) std::cout << "* * * I have not found '" << l->layerType() << "' layer in the old layers * * *\n";
    return found;
}
void ascendLayers(std::vector<Layer*>& oldLayers, std::vector<Layer*>& newLayers, int& n, bool& l) {
    int index = 0;
    //std::cout << "\n* * * Going through the newly formed layers * * *\n";
    for (unsigned int i = 0; i < newLayers.size(); i++) {
        //index = 0;
        if (newLayers[i] != nullptr) {
            //std::cout << "* * * The current new layer is: " << *(newLayers[i]) << " * * *\n";
            if (linSearch(oldLayers, newLayers[i], index, i)) {
                //std::cout << "* * * The #" << index + 1 << " old layer's thickness + the #" << i + 1 << " newly formed layer's thickness = " << oldLayers[index]->layerThickness() + oldLayers[i]->layerThickness() << " * * *\n";
                if (oldLayers[index]->layerThickness() + oldLayers[i]->layerThickness() > 0.5) {
                    //std::cout << "* * * We add them together cause they were greater than 0.5 * * *\n";
                    //std::cout << "* * * #" << index + 1 << " old layer thickness (" << oldLayers[index]->layerThickness() << ") + #" << i + 1 << " new layer thickness (" << newLayers[i]->layerThickness() << ") = " << oldLayers[index]->layerThickness() + newLayers[i]->layerThickness() << " * * *\n";
                    oldLayers[index]->setLayerThickness(oldLayers[index]->layerThickness() + newLayers[i]->layerThickness());
                }
            }
            else {
                n++;
                oldLayers.resize(n);
                oldLayers[n - 1] = newLayers[i];
                delete(newLayers[i]);
                newLayers.erase(newLayers.begin() + i);
                //std::cout << "* * * We increase n, resize old layers, set the last element of old layers to the new layer and than remove from new layers the current one * * *\n";
            }
        }
    }
    //std::cout << "\n* * * Going through the old layers * * *\n";
    for (unsigned int i = 0; i < oldLayers.size(); i++) {
        //std::cout << "* * * The current old layer is '" << *(oldLayers[i]) << "' * * *\n";
        //index = 0;
        //if (oldLayers[i] != nullptr) {
        if (oldLayers[i]->layerThickness() < 0.5) {
            //std::cout << "! ! ! This old layer was below 0.5 km ! ! !\n";
            if (linSearch(oldLayers, oldLayers[i], index, i)) {
                //if (oldLayers[index]->layerThickness() + oldLayers[i]->layerThickness() > 0.5) {
                    //std::cout << "* * * We add them together cause they were greater than 0.5 * * *\n";
                    //std::cout << "* * * #" << index + 1 << " old layer thickness (" << oldLayers[index]->layerThickness() << ") + #" << i + 1 << " old layer thickness (" << oldLayers[i]->layerThickness() << ") = " << oldLayers[index]->layerThickness() + newLayers[i]->layerThickness() << " * * *\n";
                    oldLayers[index]->setLayerThickness(oldLayers[index]->layerThickness() + oldLayers[i]->layerThickness());
                //}
                //else oldLayers.erase(oldLayers.begin() + index);
            }
            //else {
                n--;
                oldLayers[i]->destroy();
                delete(oldLayers[i]);
                oldLayers.erase(oldLayers.begin() + i);
                //std::cout << "We deleted this layer from old layers!\n";
                l = true;
                break;
                //oldLayers.resize(n);
            //}
        }
        //}
    }
}
void step(int& n, std::string w, std::vector<Layer*>& originals, std::vector<Layer*>& oldLayers, std::vector<Layer*>& newLayers, bool &l, int& dayNum) {
        for (unsigned int i = 0; i < w.length(); i++) {
                std::cout << "---------------------------------\n";
                std::cout << "| Day #" << dayNum++ << ", today's weather is " << w[i] << " |\n";
                std::cout << "---------------------------------\n\n";
                testNewData(oldLayers, n);
                //std::cout << "\n\n+ + + ...T R A N S F O R M I N G... + + +\n\n";
                transformLayers(n, w[i], oldLayers, newLayers);
                //std::cout << "\n\n+ + + ...A S C E N D I N G... + + +\n\n";
                ascendLayers(oldLayers, newLayers, n, l);
                if (l) break;
                /*if (!originalLayersAllExist(originals)) {
                    l = true;
                    break;
                }*/

            //testNewData(oldLayers, n);
           /* std::cout << "---Newly formed layers: \n";
            testNewData(newLayers, n);*/
        }
}
bool originalLayersAllExist(std::vector<Layer*> l) {
    bool allExist = true;
    //std::cout << "A volt isOver-nek atadott tomb hossza: " << l.size() << std::endl;
    if (l.size() > 0) {
        for (unsigned int i = 0; i < l.size(); i++) {
            if (l[i]->layerThickness() <= 0.5) {
                l[i]->destroy();
                allExist = false;
                break;
            }
        }
    }
    else allExist = false;
   /* for (unsigned int i = 0; i < l.size(); i++) {
        if (!l[i]->doesExist()) {

            break;
        }
    }*/
    return allExist;
}
