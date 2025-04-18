## Reversed Z infinite Projection Matrix

This is a proof of concept of implementing a reversed z projection matrix,
optionally with an infinite far plane. This also changes the depth buffer format from D32 to D32F.

The fabulous graphics mod has been modified to work with rev-z, as such it doesn't it's now broken in vanilla.

To choose which mode is used supply the `farz.mode` JVM argument as:
- `-Dfarz.mode=infinite` for reversed z with an infinite far z
- `-Dfarz.mode=reverse` for reversed z
- `-Dfarz.mode=vanilla` for vanilla

Note: View Bobbing and Nausea won't work correctly because I'm lazy, see `FrustumMixin`

For demonstration purposes I've also removed all fog.

To see the differences for yourself I recommend using waterlogged leaves at ~128 blocks and looking at them through a spyglass.
With vanilla rendering there is obvious z-fighting, with rev-z and inf-z there isn't any.

## Comparison

Leaves at a distance of 128 blocks

### Vanilla
![vanilla.png](assets/vanilla.png)

## Reverse Z
![rev-z.png](assets/rev-z.png)

## Reverse Z w/ infinite far z
![rev-inf.z.png](assets/rev-inf.z.png)

At 512 blocks away (maximum vanilla view distance) there is also no z-fighting with reversed z.

To see the effects of the infinite far z teleport up (f.e. 1.000.000 blocks), and look down with a spy glass and the smallest fov.
You'll still be able to see the clouds.
(**Set the time to midnight!** You can't see them white-on-white at noon.)