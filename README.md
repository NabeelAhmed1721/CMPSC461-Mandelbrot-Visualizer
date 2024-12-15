# Mandelbrot Visualizer

![mandelbrot_render (1)](https://github.com/user-attachments/assets/e7e89be5-14b3-4b1b-8d73-8d5f7e6b78ce)

A Mandelbrot set visualizer written in Java for the CMPSC 461 class at Penn State University. This application allows users to explore the fascinating Mandelbrot set using three different rendering engines:

1. **Sequential Rendering**
2. **Multithreaded Rendering**
3. **GPU Rendering (using OpenCL)**

## Prerequisites

To run this project, you need the following installed:

- Java Development Kit (JDK) 8 or later
- JOCL library (should already be included in source code)
- OpenCL runtime (required for GPU rendering)

## Directory Structure

```plaintext
.
├── MandelbrotVisualizer.java   # Source code for the application
├── lib/                        # Library files
│   ├── jocl.jar                # JOCL library
│   └── native/                 # Native OpenCL libraries
└── README.md                   # Project documentation
```

## How to Compile and Run

To compile and run the application with the required JOCL dependencies, use the following script:

```bash
javac -cp ".;lib/jocl.jar" *.java
java -cp ".;lib/jocl.jar" -Djava.library.path=lib/native MandelbrotVisualizer
```

## Usage

When running the application, you can choose between the three rendering engines:

- **Sequential Rendering:** Best for understanding the computation process.
- **Multithreaded Rendering:** Improves performance by utilizing multiple CPU cores.
- **GPU Rendering:** Leverages the power of your GPU for lightning-fast calculations (requires OpenCL support).

The rendering engine can be selected via configuration in the source code. Examples of each engine are commented.

## Dependencies

- [JOCL](http://www.jocl.org/): Java bindings for OpenCL

Ensure that the JOCL library and the corresponding native binaries are placed in the `lib` directory as described above.

## Troubleshooting

### Missing JOCL Library
Ensure that the `jocl.jar` file is located in the `lib` folder, and the native OpenCL libraries are in `lib/native`.

### OpenCL Not Found
Ensure that your system has OpenCL runtime installed. Most modern GPUs come with OpenCL support, but you may need to install drivers from your GPU vendor.

### Unsupported GPU
If your GPU does not support OpenCL, you can use the sequential or multithreaded rendering engines instead.

## Authors

- **Kevin Yaurincela**  
  *Institution*: Penn State University  
  *Email*: [kmy5294@psu.edu](mailto:kmy5294@psu.edu)

- **Nabeel Ahmed**  
  *Institution*: Penn State University  
  *Email*: [nza5352@psu.edu](mailto:nza5352@psu.edu)

- **Tim Lee**  
  *Institution*: Penn State University  
  *Email*: [txl5529@psu.edu](mailto:txl5529@psu.edu)

- **Thomas Snyder**  
  *Institution*: Penn State University  
  *Email*: [tds5642@psu.edu](mailto:tds5642@psu.edu)
