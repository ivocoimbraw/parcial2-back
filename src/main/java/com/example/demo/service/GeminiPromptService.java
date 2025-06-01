package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class GeminiPromptService {

  private static final String PROMPT_TEMPLATE = """
      Eres un generador de componentes Angular especializado.

      Tienes el siguiente objeto llamado initialJsonConfig:

      %s

      Con esa información debes generar un componente de Angular llamado crud-table que cumpla las siguientes reglas estrictas:

      1. HTML...
      2. TypeScript...
      3. CSS...

      Para el componente TS, debes asegurarte de que el componente contenga las siguientes importaciones:
      import { FormsModule } from '@angular/forms';
      import { CommonModule } from '@angular/common';
      imports: [FormsModule, CommonModule]
      También asegurate de que el componente TS que el config venga por defecto dentro del componente como objeto.


      ⚡ Formato de la respuesta:
      json
      {
        "html": "CÓDIGO HTML AQUÍ",
        "ts": "CÓDIGO TS AQUÍ",
        "css": "CÓDIGO CSS AQUÍ"
      }
      """;

  public String buildPrompt(String initialJsonConfig) {
    return String.format(PROMPT_TEMPLATE, initialJsonConfig);
  }

  private static final String PROMPT_TEMPLATE_IMAGE = """
Eres un asistente que genera un JSON basado en la estructura y contenido de una imagen, siguiendo estrictamente este esquema:
interface ComponentNode {
id: string; // UUID
type: "text" | "button" | "textField";
properties: Record<string, any>;
children: ComponentNode[];
style?: Style;
position?: { x: number; y: number };
}
interface Style {
fontSize: number;
borderRadius: number;
color: string; // formato hexadecimal, e.g. "#000000"
backgroundColor: string; // formato hexadecimal
width: number;
height: number;
}
Tipos permitidos para type:
const COMPONENT_TYPES = {
TEXT: "text",
BUTTON: "button",
TEXT_FIELD: "textField",
};
Ejemplos de properties según type:
Texto (text): { "text": "Texto" }
Botón (button): { "text": "Botón", "variant": "primary" }
Campo de texto (textField): {"hint": "Texto de ayuda" }
Ejemplo de salida JSON:
{
"id": "d45222d2-cba9-4423-b7f3-8d56cd10ed85",
"type": "text",
"properties": {
"text": "Text,
},
"children": [],
"style": {
"fontSize": 16,
"borderRadius": 5,
"color": "#000000",
"backgroundColor": "#ffffff",
"width": 35,
"height": 30
},
"position": {
"x": 0,
"y": 0
}
}
Instrucciones importantes:
Siempre genera única y exclusivamente un JSON válido con esta estructura.
El campo id debe ser un UUID válido.
Usa solo los tipos indicados en COMPONENT_TYPES.
Los valores de color deben estar en formato hexadecimal.
children es un arreglo que puede contener otros nodos con la misma estructura.
No agregues texto, explicaciones ni formatos adicionales en la respuesta, solo el JSON.Cada componente debe de tener position
Cada componente este separado, no este dentro de un children o un contenedor que sean individual
                              """;

  public String buildPromptImage() {
    return PROMPT_TEMPLATE_IMAGE;
  }

}
