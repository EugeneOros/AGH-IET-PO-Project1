package project1;

import java.util.Random;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    public static MapDirection getRandom() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }

    public MapDirection next() {
        switch (this) {
            case NORTH: {
                return MapDirection.NORTHEAST;
            }
            case SOUTH: {
                return MapDirection.SOUTHWEST;
            }
            case WEST: {
                return MapDirection.NORTHWEST;
            }
            case EAST: {
                return MapDirection.SOUTHEAST;
            }
            case NORTHEAST: {
                return MapDirection.EAST;
            }
            case NORTHWEST: {
                return MapDirection.NORTH;
            }
            case SOUTHEAST: {
                return MapDirection.SOUTH;
            }
            case SOUTHWEST: {
                return MapDirection.WEST;
            }
            default: {
                return null;
            }
        }
    }

    public MapDirection previous() {
        switch (this) {
            case NORTH: {
                return MapDirection.NORTHWEST;
            }
            case SOUTH: {
                return MapDirection.SOUTHEAST;
            }
            case WEST: {
                return MapDirection.SOUTHWEST;
            }
            case EAST: {
                return MapDirection.NORTHEAST;
            }
            case NORTHEAST: {
                return MapDirection.NORTH;
            }
            case NORTHWEST: {
                return MapDirection.WEST;
            }
            case SOUTHEAST: {
                return MapDirection.EAST;
            }
            case SOUTHWEST: {
                return MapDirection.SOUTH;
            }
            default: {
                return null;
            }
        }
    }

    public MapDirection opposite() {
        switch (this) {
            case NORTH: {
                return MapDirection.SOUTH;
            }
            case SOUTH: {
                return MapDirection.NORTH;
            }
            case WEST: {
                return MapDirection.EAST;
            }
            case EAST: {
                return MapDirection.WEST;
            }
            case NORTHEAST: {
                return MapDirection.SOUTHWEST;
            }
            case NORTHWEST: {
                return MapDirection.SOUTHEAST;
            }
            case SOUTHEAST: {
                return MapDirection.NORTHWEST;
            }
            case SOUTHWEST: {
                return MapDirection.NORTHEAST;
            }
            default: {
                return null;
            }
        }
    }


    public Vector2d toUnitVector() {
        switch (this) {
            case NORTH: {
                return new Vector2d(0, 1);
            }
            case SOUTH: {
                return new Vector2d(0, -1);
            }
            case WEST: {
                return new Vector2d(-1, 0);
            }
            case EAST: {
                return new Vector2d(1, 0);
            }
            case NORTHEAST: {
                return new Vector2d(1, 1);
            }
            case NORTHWEST: {
                return new Vector2d(-1, 1);
            }
            case SOUTHEAST: {
                return new Vector2d(1, -1);
            }
            case SOUTHWEST: {
                return new Vector2d(-1, -1);
            }
            default: {
                return null;
            }
        }
    }
}
