# omegga-brsscalar
 Allows users to scale builds
 
## Installation
 
```
omegga install gh:ZombieStriker/brsscalar
```
## REQUIRES
-Java 1.8 or higher

## How to use
First, you must be the host to use any of the commands.

Start by having the save you wish to upscale in the /Saves/ folder. You can load the save and use 
!brsscalar:save to scave all the bricks in the world. From there, use !brsscalar (save) (scalar) to upscale
or downscale the entire build by that scalar.

#Notes
* It currently only stretches the brick up to the brick's maximum. If the scalar needs to stretch it to be
larger than the maximum size, it will split the brick up into two smaller bricks to accomplish the stretch.
However, it does so in a cube, so special wedges are not correctly stretched.
* There is an issue where you sometimes have to run the commands twice in order to work. IDK why.
* Because it uses Java/RPC calls to load the bricks, it is slow. You may need to wait a bit for large builds.
