package sample.Engine.Core;

import javafx.scene.shape.TriangleMesh;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class ObjReader {

    private final List<Float> vertices;
    private final List<Integer> faces;

    public ObjReader(String path)
    {

        vertices = new LinkedList<>();
        faces = new LinkedList<>();

        try {
            InputStream in = getClass().getResourceAsStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            String[] coords;
            while ((line = reader.readLine()) != null)
            {
                if (line.startsWith("v "))
                {
                    coords = line.split(" ", 4);
                    vertices.add(Float.parseFloat(coords[1]));
                    vertices.add(Float.parseFloat(coords[2]));
                    vertices.add(Float.parseFloat(coords[3]));
                    continue;
                }

                if (line.startsWith("f "))
                {
                    coords = line.split(" ", 4);
                    faces.add(Integer.parseInt( coords[1].split("/")[0] ));
                    faces.add(Integer.parseInt( coords[2].split("/")[0] ));
                    faces.add(Integer.parseInt( coords[3].split("/")[0] ));
                }
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("3D Object " + path + " could not be Loaded!");
        }

    }

    public TriangleMesh createMesh()
    {
        TriangleMesh mesh = new TriangleMesh();

        mesh.getTexCoords().addAll(0,0);

        for (Float vertex : vertices) {
            mesh.getPoints().addAll(vertex);
        }

        for (Integer face : faces) {
            mesh.getFaces().addAll(face - 1, 0);
        }

        return mesh;
    }

}
