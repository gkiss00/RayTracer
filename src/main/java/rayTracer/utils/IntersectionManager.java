package rayTracer.utils;

import rayTracer.enums.CapacityTypeEnum;
import rayTracer.enums.NoiseDimensionEnum;
import rayTracer.enums.ObjectTypeEnum;
import rayTracer.enums.PatternTypeEnum;
import rayTracer.math.Line3D;
import rayTracer.objects.Obj;
import rayTracer.objects.baseObjects.BaseObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntersectionManager {
    public static void getIntersections(Line3D ray, List<Obj> objects, List<Intersection> intersections) {
        for (Obj object : objects) {
            try {
                object.hit(ray, intersections);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        sortIntersections(intersections);
    }

    // when intersections arrive here, THEY MUST BE SORTED
    public static void preProcessIntersections(List<Intersection> intersections, List<Intersection> blackIntersections) {
        // 1. Remove intersections of empty objects contained in full objects
        removeIntersectionsInFullObjects(intersections);
        // 2. Fusion consecutive full objects of same color
        fusionFullObjects(intersections);
        // 3. Remove empty intersection in black objects
        removeIntersectionInBlackObjects(intersections, blackIntersections);
        // 4. Remove and create intersections with black objects
        fusionFullObjects(blackIntersections);
        if(intersections.size() != 0 && blackIntersections.size() != 0)
            createNewIntersections(intersections, blackIntersections);
    }

    private static void removeIntersectionsInFullObjects(List<Intersection> intersections) {
        List<Intersection> remainingIntersections = new ArrayList<>(intersections.size());
        Map<Integer, Integer> map = new HashMap<>();

        for (Intersection intersection : intersections) {
            if (intersection.getObject().getCapacity() == CapacityTypeEnum.FULL) {
                remainingIntersections.add(intersection);
                if (map.containsKey(intersection.getObject().getId()))
                    map.remove(intersection.getObject().getId());
                else
                    map.put(intersection.getObject().getId(), 1);
            } else {
                if (map.isEmpty())
                    remainingIntersections.add(intersection);
            }
        }

        intersections.clear();
        intersections.addAll(remainingIntersections);
    }

    // when arriving here, nor more EMPTY intersections are in FULL objects
    // when two full objects are combined, fusion their intersections
    private static void fusionFullObjects(List<Intersection> intersections) {
        List<Intersection> remainingIntersections = new ArrayList<>(intersections.size());
        Map<Integer, Integer> map = new HashMap<>();
        Intersection firstInserted = null;

        for (Intersection intersection : intersections) {
            int objectId = intersection.getObject().getId();

            if (intersection.getObject().getCapacity() == CapacityTypeEnum.FULL) {
                // keep track of the first full object
                if(map.isEmpty()) {
                    firstInserted = intersection;
                    remainingIntersections.add(intersection);
                }
                if (map.containsKey(objectId)) {
                    map.remove(objectId);
                } else {
                    map.put(objectId, 1);
                }
                if(map.isEmpty()) {
                    intersection.setObject(firstInserted.getObject());
                    remainingIntersections.add(intersection);
                }
            }
            // add all empty intersections
            else {
                remainingIntersections.add(intersection);
            }
        }

        intersections.clear();
        intersections.addAll(remainingIntersections);
    }

    private static void removeIntersectionInBlackObjects(List<Intersection> baseIntersections, List<Intersection> blackIntersections) {
        List<Intersection> emptyIntersections = new ArrayList<>(blackIntersections);
        List<Intersection> fullIntersections = new ArrayList<>();
        for(Intersection intersection: baseIntersections) {
            if (intersection.getObject().getCapacity() == CapacityTypeEnum.EMPTY) {
                emptyIntersections.add(intersection);
            } else {
                fullIntersections.add(intersection);
            }
        }
        sortIntersections(emptyIntersections);
        //removeIntersectionsInFullObjects(emptyIntersections);
        baseIntersections.clear();
        baseIntersections.addAll(fullIntersections);
        for(Intersection intersection: emptyIntersections) {
            if (intersection.getObject().getCapacity() == CapacityTypeEnum.EMPTY) {
                baseIntersections.add(intersection);
            }
        }
        sortIntersections(baseIntersections);
    }

    private static void createNewIntersections(List<Intersection> baseIntersections, List<Intersection> blackInterserctions) {
        List<Intersection> allIntersections = new ArrayList<>(baseIntersections.size() + blackInterserctions.size());
        allIntersections.addAll(baseIntersections);
        allIntersections.addAll(blackInterserctions);
        sortIntersections(allIntersections);
        List<Intersection> res = new ArrayList<>(baseIntersections.size() + blackInterserctions.size());
        int i = 0;
        int baseI = 0;
        boolean inBlack = false;
        boolean inBase = false;

        while(i < allIntersections.size()) {
            Intersection intersection = allIntersections.get(i);
            ObjectTypeEnum objectType = intersection.getObject().getType();
            // keep all empty for now
            if(intersection.getObject().getCapacity() == CapacityTypeEnum.EMPTY) {
                ++i;
                ++baseI;
                baseI = Math.min(baseI, baseIntersections.size() - 1);
                res.add(intersection);
                continue;
            }

            if(!inBase && !inBlack) {
                if(objectType == ObjectTypeEnum.BASE) {
                    inBase = true;
                    res.add(intersection);
                } else {
                    inBlack = true;
                }
            } else if (inBase && !inBlack) {
                if(objectType == ObjectTypeEnum.BASE) {
                    inBase = false;
                    res.add(intersection);
                } else {
                    inBlack = true;
                    intersection.setColor(baseIntersections.get(baseI).getColor());
                    BaseObject baseObject = (BaseObject)baseIntersections.get(baseI).getObject();
                    if(baseObject.getPattern() == PatternTypeEnum.NOISE && baseObject.getNoiseDimension() == NoiseDimensionEnum.DIMENSION_3D) {
                        Color c = baseObject.getColorAt(intersection.getPointOfIntersection());
                        intersection.setColor(c);
                    }
                    intersection.setObject(baseIntersections.get(baseI).getObject());
                    res.add(intersection);
                }
            } else if (!inBase && inBlack) {
                if(objectType == ObjectTypeEnum.BASE) {
                    inBase = true;
                } else {
                    inBlack = false;
                }
            } else if (inBase && inBlack) {
                if(objectType == ObjectTypeEnum.BASE) {
                    inBase = false;
                } else {
                    inBlack = false;
                    intersection.setColor(baseIntersections.get(baseI).getColor());
                    BaseObject baseObject = (BaseObject)baseIntersections.get(baseI).getObject();
                    if(baseObject.getPattern() == PatternTypeEnum.NOISE && baseObject.getNoiseDimension() == NoiseDimensionEnum.DIMENSION_3D) {
                        Color c = baseObject.getColorAt(intersection.getPointOfIntersection());
                        intersection.setColor(c);
                    }
                    intersection.setObject(baseIntersections.get(baseI).getObject());
                    res.add(intersection);
                }
            }
            if(objectType == ObjectTypeEnum.BASE && i != 0) {
                ++baseI;
                baseI = Math.min(baseI, baseIntersections.size() - 1);
            }
            ++i;
        }
        baseIntersections.clear();
        blackInterserctions.clear();
        baseIntersections.addAll(res);
        allIntersections.clear();
    }

    private static void removeOutsideIntersections(List<Intersection> baseIntersections, List<Intersection> whiteIntersections) {
        List<Intersection> all = new ArrayList<>(baseIntersections.size() + whiteIntersections.size());
        List<Intersection> remainingIntersections = new ArrayList<>();
        Map<Integer, Integer> whiteMap = new HashMap<>();
        all.addAll(baseIntersections);
        all.addAll(whiteIntersections);
        sortIntersections(all);
        for (Intersection intersection : all) {
            ObjectTypeEnum objectType = intersection.getObject().getType();
            int objectId = intersection.getObject().getId();
            // here, we are outside white object => all intersections should be removed
            if(whiteMap.isEmpty()) {
                if(objectType == ObjectTypeEnum.WHITE) {
                    whiteMap.put(objectId, 1);
                } else if (objectType == ObjectTypeEnum.BASE) {

                }
            }
            // here, we are INSIDE white object
            else {
                // we keep this intersection
                if(objectType == ObjectTypeEnum.BASE) {
                    whiteMap.put(intersection.getObject().getId(), 1);
                    remainingIntersections.add(intersection);
                } else if (objectType == ObjectTypeEnum.WHITE) {
                    if(whiteMap.containsKey(objectId)) {
                        whiteMap.remove(objectId);
                    } else {
                        whiteMap.put(objectId, 1);
                    }
                }
            }
        }
    }

    private static void sortIntersections(List<Intersection> intersections) {
        int size = intersections.size();
        for (int i = 0; i < size - 1; ++i) {
            boolean swapped = false;
            for (int j = 0; j < size - i - 1; ++j) {
                if (intersections.get(j).getDistanceFromCamera() > intersections.get(j + 1).getDistanceFromCamera()) {
                    Intersection tmp = intersections.get(j);
                    intersections.set(j, intersections.get(j + 1));
                    intersections.set(j + 1, tmp);
                    swapped = true;
                }
            }
            if(!swapped)
                break;
        }
    }
}
