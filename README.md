# ElectromagneticFlow

ElectromagneticFlow is a wave solver designed for simulating various types of wave phenomena, with a particular focus on electromagnetic waves. The tool offers a high degree of interactivity, making it suitable for both educational and research purposes. The simulations are built to be physically accurate, providing a robust platform for exploring wave behaviors in different contexts.

## Features

- **Multiple Simulation Methods**: Supports a variety of simulation techniques for solving wave equations, including both analytical and numerical approaches.
- **Interactive Visualizations**: Real-time, interactive visualizations of electromagnetic waves and their interactions with different media and boundaries.
- **Physically Accurate**: Designed with a focus on physical rigor to ensure reliable and precise results for educational and research applications.
- **User-Friendly Interface**: Easy-to-use controls for adjusting simulation parameters, including wave sources, boundary conditions, and more.

## Use Cases

- **Photonics Research**: Investigate wave propagation, interference, and diffraction in electromagnetic systems.
- **Educational Tool**: Aimed at helping students and researchers build a deeper understanding of wave phenomena in physics.

## Building the web application

The tools you will need to build the project are:

* Eclipse, Oxygen version.
* GWT plugin for Eclipse.

Install "Eclipse for Java developers" from [here](https://www.eclipse.org/downloads/packages/). To add the GWT plugin for Eclipse follow the instructions [here](https://gwt-plugins.github.io/documentation/gwt-eclipse-plugin/Download.html).

This repository is a project folder for your Eclipse project space. Once you have a local copy you can then build and run in development mode or build for deployment. Running in super development mode is done by clicking on the "run" icon on the toolbar and choosing http://127.0.0.1:8888/Ripple.html from the "Development Mode" tab which appears. Building for deployment is done by selecting the project root node and using the GWT button on the Eclipse taskbar and choosing "GWT Compile Project...".

GWT will build its output in to the "war" directory. In the "war" directory the file "iframe.html" is loaded as an iFrame in to the spare space at the bottom of the right hand pannel. It can be used for branding etc.

## Deployment of the web application

* "GWT Compile Project..." as explained above. This will put the outputs in to the "war" directory in the Eclipse project folder. You then need to copy everything in the "war" directory, except the "WEB-INF" directory, on to your web server.

The link for the full-page version of the application is now:
`http://<your host>/<your path>/Ripple.html`

Just for reference the files should look like this

```
-+ Directory containing the front page (eg "ripple")
  +- Ripple.html - full page version of application
  ++ ripple (directory)
   +- various files built by GWT
   +- examples (directory)
   +- setuplist.txt (index in to example directory)
```

## Embedding

You can link to the full page version of the application using the link shown above.

If you want to embed the application in another page then use an iframe with the src being the full-page version.

You can add query parameters to link to change the applications startup behaviour. The following are supported:
```
.../Ripple.html?rol=<string> // Load the example from the URL
.../Ripple.html?startExample=<filename> // Loads the file named "filename" from the "examples" directory
.../Ripple.html?colorScheme=rrggbb,rrggbb,... // 8 hex color specifications for walls, + waves, - waves, 0 waves, +,-,0 waves in media, sources
```
## Building an Electron application

The [Electron](https://electronjs.org/) project allows web applications to be distributed as local executables for a variety of platforms. This repository contains the additional files needed to build Ripple as an Electron application.

The general approach to building an Electron application for a particular platform is documented [here](https://electronjs.org/docs/tutorial/application-distribution). The following instructions apply this approach to Ripple.

To build the Electron application:
* Compile the application using GWT, as above.
* Download and unpack a [pre-built Electron binary directory](https://github.com/electron/electron/releases) version 9.3.2 for the target platform.
* Copy the "app" directory from this repository to the location specified [here](https://electronjs.org/docs/tutorial/application-distribution) in the Electron binary directory structure.
* Copy the "war" directory, containing the compiled Ripple application, in to the "app" directory the Electron binary directory structure.
* Run the "Electron" executable file. It should automatically load Ripple.

Thanks to @Immortalin for the initial work in applying Electron to CircuitJS1, which was then applied to Ripple.


## License

This project is licensed under the GNU General Public License, version 2 or later.

## Acknowledgements

I would like to express my gratitude to Paul Falstad for his original work on the Ripple Tank simulation, which laid the foundation for this project. His contributions have been invaluable, and this project builds upon his legacy to provide a more interactive and physically rigorous simulation tool.